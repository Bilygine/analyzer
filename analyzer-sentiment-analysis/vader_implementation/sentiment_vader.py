from vaderSentiment.vaderSentiment import SentimentIntensityAnalyzer 

# --- examples -------
sentences = ["VADER is smart, handsome, and funny.",  # positive sentence example
             "VADER is smart, handsome, and funny!",  # punctuation emphasis handled correctly (sentiment intensity adjusted)
             "VADER is very smart, handsome, and funny.", # booster words handled correctly (sentiment intensity adjusted)
             "VADER is VERY SMART, handsome, and FUNNY.",  # emphasis for ALLCAPS handled
             "VADER is VERY SMART, handsome, and FUNNY!!!", # combination of signals - VADER appropriately adjusts intensity
             "VADER is VERY SMART, uber handsome, and FRIGGIN FUNNY!!!", # booster words & punctuation make this close to ceiling for score
             "VADER is not smart, handsome, nor funny.",  # negation sentence example
             "The book was good.",  # positive sentence
             "At least it isn't a horrible book.",  # negated negative sentence with contraction
             "The book was only kind of good.", # qualified positive sentence is handled correctly (intensity adjusted)
             "The plot was good, but the characters are uncompelling and the dialog is not great.", # mixed negation sentence
             "Today SUX!",  # negative slang with capitalization emphasis
             "Today only kinda sux! But I'll get by, lol", # mixed sentiment example with slang and constrastive conjunction "but"
             "Make sure you :) or :D today!",  # emojis handled
             "Not bad at all"  # Capitalized negation
             ]

analyzer = SentimentIntensityAnalyzer()
for sentence in sentences:
    vs = analyzer.polarity_scores(sentence)
    print("{:-<65} {}".format(sentence, str(vs)))



# # from nltk import sent_tokenize
# # from nltk.tokenize import PunktSentenceTokenizer
# # from nltk.corpus import reuters

# from flask import Flask, request
# from flask_api import status

# # app = Flask(__name__)

# # @app.route("/sentiment/vader/python/formatted", methods = ['POST'])
# def sentiment_vader():
# 	# input_data = request.json["content"]
# 	input_data = 'I have, myself, full confidence that if all do their duty, if nothing is neglected, and if the best arrangements are made, as they are being made, we shall prove ourselves once again able to defend our Island home, to ride out the storm of war, and to outlive the menace of tyranny, if necessary for years, if necessary alone..'

# 	# nltk.downloader.download('vader_lexicon')

# 	# sentences = sent_tokenize(input_data)
# 	sentences = input_data
# 	sid = SentimentIntensityAnalyzer()
# 	global_number_sentences = len(sentences)
# 	data = []
# 	for i in range(0, global_number_sentences):
# 		cur_content = i
# 		sentiment_dict = sid.polarity_scores(cur_content) 
# 		cur_dict = {"index": cur_content,
# 		"sentence" : sentences[i],
# 		"negative" : sentiment_dict['neg'] * 100,
# 		"neutral" : sentiment_dict['neu'] * 100,
# 		"positive" : sentiment_dict['pos']*100,
# 		"score": sentiment_dict['compound']}
# 		data.append(cur_dict)	
# 	global_data = {'sentences_results' : data}
# 	json_data = json.dumps(global_data)
# 	return json_data, status.HTTP_200_OK

# sentiment_vader()
