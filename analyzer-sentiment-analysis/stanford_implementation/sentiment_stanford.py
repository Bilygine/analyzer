import json
import re
from pycorenlp import StanfordCoreNLP
from flask import Flask, request

app = Flask(__name__)

@app.route("/sentiment/stanford/python/formatted", methods = ['POST'])
def sentiment_stanford():
	input_data = request.json["content"]

	nlp = StanfordCoreNLP('http://localhost:9076')
	content = nlp.annotate(input_data, properties={'annotators': 'sentiment', 'outputFormat': 'json', 'timeout': 120000})


	formatted_content = re.sub('0,([0-9]+)', '0.\\1', content)
	json_data = json.loads(formatted_content)
	sentences = json_data["sentences"]

	# return str(json_data)

	global_number_sentences = len(sentences)

	global_results = {'sentence_count' : global_number_sentences}

	data = []
	for i in range(0, global_number_sentences):
		cur_content = sentences[i]

		cur_sentence = ''
		for j in cur_content["tokens"]:
			cur_sentence = cur_sentence + ' ' + j["word"] 

		cur_score = cur_content["sentimentValue"]
		cur_sentiment = cur_content["sentiment"]
		
		cur_very_negativity = cur_content['sentimentDistribution'][0]
		cur_negativity = cur_content['sentimentDistribution'][1]
		cur_neutrality = cur_content['sentimentDistribution'][2]
		cur_positivity = cur_content['sentimentDistribution'][3]
		cur_very_positivity = cur_content['sentimentDistribution'][4]

		cur_score_accurate = (cur_very_negativity * -1) + (cur_negativity * -0.5) + (cur_neutrality * 0) + (cur_positivity * 0.5) + (cur_very_positivity * 1)

		cur_dict = {'index' : i,
		'sentence' : cur_sentence,
		'score_raw' : cur_score,
		'score' : round(cur_score_accurate, 2),
		'sentiment' : cur_sentiment,
		'very_negativity' : round(cur_very_negativity, 2),
		'negativity' : round(cur_negativity, 2),
		'neutrality' : round(cur_neutrality, 2),
		'positivity' : round(cur_positivity, 2),
		'very_positivity' : round(cur_very_positivity, 2)}
		data.append(cur_dict)
	
	global_data = {'sentences_results' : data, 'global_results' : global_results}

	json_data = json.dumps(global_data)
	
	return json_data, 200

