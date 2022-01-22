import six
import sys
import io
import os
import json

from google.cloud import language
from google.cloud.language import enums
from google.cloud.language import types

from flask import Flask, request
from os import environ

cur_service = "[Sentiment Google Service] "

app = Flask(__name__)

@app.route("/sentiment/google/python/formatted", methods = ['POST'])
def sentiment_google_formatted():
	"""
		Analyse and return multiple informations about sentiment of a text passed in entry.

		Input: 
		{"content": "I am happy. I am sad."}

		Output:
		{"sentences_results": 
			[
				{"index": 0,"sentence": "I am happy.","score": 0.8,"sentiment": "positive","magnitude": 0.8},
		        {"index": 1,"sentence": "I am sad.","score": -0.2,"sentiment": "slighty negative","magnitude": 0.2}
		    ],
	    	"global_results": {"score": 0.2,"sentiment": "neutral negative","magnitude": 1,"sentence_count": 2,"power": "neutral strong"
	    }
	}
	"""

	if (check_credentials() == False):
		return cur_service + "Please verify your GOOGLE_APPLICATION_CREDENTIALS env variable is properly set.", 400

	# Check whether input is well formatted.
	try:
		input_data = request.json["content"]
	except:
		return cur_service + "Current input is empty", 400

	# Check whether input text len is
	if (len(input_data) == 0):
		return cur_service + "Current input text length is 0", 400

	response = send_data(input_data)

	sentences = response.sentences
	document_sentiment = response.document_sentiment

	try:
		# Add global sentiment informations for the entire content
		sentence_count = len(sentences)
		text_magnitude = round(document_sentiment.magnitude, 2)
		text_score = round(document_sentiment.score, 2)
		text_sentiment_power = text_magnitude / sentence_count

		score_words = ['extremely negative','very negative','negative','slightly negative','slightly positive','positive','very positive','extremely positive']
		text_magnitude_power = ['extremely weak','very weak','weak','slightly weak','slightly strong','strong','very strong','extremely strong']

		# Add a word to define global sentiment and magnitude depending on their numerical value
		text_sentiment = score_words[int(4 * text_score + 3)]
		global_magnitude_power_word = text_magnitude_power[int(text_sentiment_power // 0.125)]

		# Create a dictionary for global results
		global_results = {'score' : round(text_score, 2),
		'sentiment' : text_sentiment,
		'magnitude' : round(text_magnitude, 2),
		'sentence_count' : sentence_count,
		'power' : global_magnitude_power_word}

		sentences_results = []
	except:
		return cur_service + "An error occured when trying to get global text results", 400

	# Loop on sentences in order to associate sentiment informations for each of them
	for index, cur_sentence in enumerate(sentences):
		try:
			# Add sentiment informations for current sentence : index of the sentence, content, score in interval [-1;1], sentiment (as word), magnitude.
			cur_score = cur_sentence.sentiment.score

			cur_dict = {'index' : index,
			'sentence' : cur_sentence.text.content,
			'score' : round(cur_score, 2),
			'sentiment' : score_words[int(4 * cur_score + 3)],
			'magnitude' : round(cur_sentence.sentiment.magnitude, 2)
			}

			# Add the cur dictionary to an array of sentence and related sentiment informations
			sentences_results.append(cur_dict)
		except:
			return cur_service + "An error occured on sentence at index " + index + " (" + cur_sentence + "). Please verify your input.", 400
	
	# Create a json and add to it global results and sentence results
	global_data = {'global_results' : global_results, 'sentences_results' : sentences_results}

	json_data = json.dumps(global_data)

	return json_data, 200

@app.route("/sentiment/google/python/raw", methods = ['POST'])
def sentiment_google_raw():
	if (check_credentials == False):
		return
	input_data = request.json["content"]
	response = send_data(input_data)
	return str(response), 200

def check_credentials():
	if environ.get('GOOGLE_APPLICATION_CREDENTIALS') is not None:
  		return True
	return False

def send_data(input_data):
	# Send the input to Google NLP service and return its response
	try:
		if isinstance(input_data, six.binary_type):
			input_data = input_data.decode('utf-8')
		type_ = enums.Document.Type.PLAIN_TEXT
		document = {'type': type_, 'content': input_data}

		client = language.LanguageServiceClient()
		response = client.analyze_sentiment(document)
		return response
	except:
		return cur_service + "An error occured when sending input to Google Cloud service", 400	