# -*- coding: utf-8 -*-

import io
import os
import csv
import os
import requests
import json

def analyze_file_content(file_name, file_content):
	result_google = requests.post(url = "http://127.0.0.1:5002/sentiment/google/python/formatted", json = {"content": file_content})
	result_textblob = requests.post(url = "http://127.0.0.1:5003/sentiment/textblob/python/formatted", json = {"content": file_content})
	result_stanford = requests.post(url = "http://127.0.0.1:5004/sentiment/stanford/python/formatted", json = {"content": file_content})

	json_google = result_google.json()
	json_textblob = result_textblob.json()
	json_stanford = result_stanford.json()

	with open('results/{}_res.csv'.format(file_name), 'w') as csvFile:
		writer = csv.writer(csvFile)

		row = ["index","google_sentence","google_score","google_sentiment","google_magnitude","textblob_sentence","textblob_score","textblob_subjectivity_score","textblob_subjectivity","textblob_sentiment","stanford_sentence","stanford_score_raw","stanford_score","stanford_very_negativity","stanford_negativity","stanford_neutrality","stanford_positivity","stanford_very_positivity"]

		writer.writerow(row)

		r = min(json_google["global_results"]["sentence_count"],json_textblob["global_results"]["sentence_count"],json_stanford["global_results"]["sentence_count"])
		for i in range(0, r):
			index = i
			google_sentence = json_google["sentences_results"][i]["sentence"]
			google_score = json_google["sentences_results"][i]["score"]
			google_sentiment = json_google["sentences_results"][i]["sentiment"]
			google_magnitude = json_google["sentences_results"][i]["magnitude"]

			textblob_sentence = json_textblob["sentences_results"][i]["sentence"]
			textblob_score = json_textblob["sentences_results"][i]["score"]
			textblob_subjectivity_score = json_textblob["sentences_results"][i]["subjectivity_score"]
			textblob_subjectivity = json_textblob["sentences_results"][i]["subjectivity"]
			textblob_sentiment = json_textblob["sentences_results"][i]["sentiment"]

			stanford_sentence = json_stanford["sentences_results"][i]["sentence"]
			stanford_score_raw = json_stanford["sentences_results"][i]["score_raw"]
			stanford_score = json_stanford["sentences_results"][i]["score"]
			stanford_very_negativity = json_stanford["sentences_results"][i]["very_negativity"]
			stanford_negativity = json_stanford["sentences_results"][i]["negativity"]
			stanford_neutrality = json_stanford["sentences_results"][i]["neutrality"]
			stanford_positivity = json_stanford["sentences_results"][i]["positivity"]
			stanford_very_positivity = json_stanford["sentences_results"][i]["very_positivity"]

			cur_row = [index,google_sentence,google_score,google_sentiment,google_magnitude,textblob_sentence,textblob_score,textblob_subjectivity_score,textblob_subjectivity,textblob_sentiment,stanford_sentence,stanford_score_raw,stanford_score,stanford_very_negativity,stanford_negativity,stanford_neutrality,stanford_positivity,stanford_very_positivity]
			writer.writerow(cur_row)

	csvFile.close()
	print('{} OK !'.format(file_name))

def list_files():
    with open("./FILE_BOX", "r") as f:
        files = f.readlines()
        files = [x.strip() for x in files]
    return files

path = './samples'
files = list_files()
for name in files:
	f = open("./samples/"+name, "r")
	curfile_content = f.read()
	analyze_file_content(name+"_sentiment", curfile_content)









