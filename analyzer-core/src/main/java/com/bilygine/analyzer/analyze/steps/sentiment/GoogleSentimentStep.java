package com.bilygine.analyzer.analyze.steps.sentiment;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.configuration.Config;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class GoogleSentimentStep extends SentimentStep {


	private static final Logger LOGGER = LogManager.getLogger(GoogleSentimentStep.class);
	//
	private static final String ROUTE = "/sentiment/google/python/formatted";

	public GoogleSentimentStep(Analyze analyze) {
		super(analyze, GoogleSentimentStep.class.getName());
	}

	class InputModel {

		String content;

		public InputModel (String content) {
			this.content = content;
		}
	}

	@Override
	public void process() throws Exception {
		String uri = Config.SERVICES_SENTIMENT_GOOGLE_HOST.stringValue() + ":" + Config.SERVICES_SENTIMENT_GOOGLE_PORT.intValue();
		HttpClient client = HttpClientBuilder.create().build();
		HttpPost httpPost = new HttpPost("http://" + uri + ROUTE);
		InputModel model = new InputModel(getText());
		LOGGER.info("Service: " + httpPost.getURI());
		//
		Gson gson = new Gson();
		//
		StringEntity params = new StringEntity(gson.toJson(model));
		httpPost.addHeader("Content-Type", "application/json");
		httpPost.setEntity(params);
		HttpResponse response = client.execute(httpPost);
		LOGGER.info(EntityUtils.toString(response.getEntity(), "UTF-8"));
	}

	@Override
	public String getDescription() {
		return "Sentiment text analysis from NLP.";
	}
}
