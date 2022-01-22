import csv
import sys
import re
import json
from flask import Flask, request
from flask_api import status
import re

from textblob import TextBlob
from textblob_fr import PatternTagger, PatternAnalyzer

from nltk import sent_tokenize
from nltk.tokenize import PunktSentenceTokenizer
from nltk.corpus import reuters

def tokenize_nltk_default(text):
	sentences = sent_tokenize(text)
	return sentences

def tokenize_nltk_reuters(text):
	pst = PunktSentenceTokenizer()
	pst.train(reuters.raw())
	sentences = pst.sentences_from_text(text)
	return sentences

app = Flask(__name__)

@app.route("/sentiment/textblob/python/formatted", methods = ['POST'])
def sentiment_textblob():
	input_data = request.json["content"]

	sentences = tokenize_nltk_default(input_data)
	content = TextBlob(input_data, pos_tagger=PatternTagger(), analyzer=PatternAnalyzer())
	# return str(content.sentences)

	global_number_sentences = len(sentences)
	global_polarity = content.sentiment[0]
	global_subjectivity = content.sentiment[1]

	polarity_words = ['very negative','negative','slighty negative','neutral negative','neutral positive','slighty positive','positive','very positive']
	subjectivity_words = ['very objective','objective','slighty objective','neutral objective','neutral subjective','slighty subjective','subjective','very subjective']

	global_polarity_word = polarity_words[int(4 * global_polarity + 3)] # -1 to 1
	global_subjectivity_word = subjectivity_words[int(8 * global_subjectivity - 1)] # 0 to 1

	global_results = {'score' : round(global_polarity, 2),
	'sentiment' : global_polarity_word,
	'subjectivity_score' : round(global_subjectivity, 2),
	'subjectivity' : global_subjectivity_word,
	'sentence_count' : global_number_sentences}

	data = []
	for i in range(0, global_number_sentences):
		cur_content = TextBlob(sentences[i])

		cur_score = cur_content.sentiment[0]
		cur_subjectivity = cur_content.sentiment[1]

		cur_dict = {'index' : i,
		'sentence' : sentences[i],
		'score' : round(cur_score, 2),
		'sentiment' : polarity_words[int(4 * cur_score + 3)],
		'subjectivity_score' : round(cur_subjectivity, 2),
		'subjectivity' : subjectivity_words[int(8 * cur_subjectivity - 1)]}

		data.append(cur_dict)

	global_data = {'sentences_results' : data, 'global_results' : global_results}
	json_data = json.dumps(global_data)
	return json_data, status.HTTP_200_OK
