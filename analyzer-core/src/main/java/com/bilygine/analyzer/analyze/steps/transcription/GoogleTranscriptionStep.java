package com.bilygine.analyzer.analyze.steps.transcription;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.result.ResultBuilder;
import com.bilygine.analyzer.configuration.Config;
import com.bilygine.analyzer.entity.error.AnalyzerError;
import com.google.api.gax.core.FixedCredentialsProvider;
import com.google.api.gax.longrunning.OperationFuture;
import com.google.auth.oauth2.ServiceAccountCredentials;
import com.google.cloud.speech.v1.*;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.*;

public class GoogleTranscriptionStep extends TranscriptionStep {

	private static final Logger LOGGER = LogManager.getLogger(GoogleTranscriptionStep.class);
	//
	private static final String NAME = "GOOGLE_SPEECH_TO_TEXT";
	private static final int TIME_LOOP = 3000;

	public GoogleTranscriptionStep(Analyze analyze) {
		super(analyze, NAME);
		String path = analyze.getAudio();
		// Validate
		if (StringUtils.isBlank(path) || (!path.startsWith("gs://") && !path.startsWith("fake://"))) {
			throw new AnalyzerError("Google Storage path is invalid: " + path);
		}
	}

	@Override
	public String getDescription() {
		return "Transcription with Speech to Text";
	}

	/**
	 * Run analyze on Google cloud and register all occurence
	 */
	@Override
	public void transcript() throws Exception {
		FixedCredentialsProvider credentialsProvider = FixedCredentialsProvider.create(getCredentials());
		SpeechSettings settings = SpeechSettings.newBuilder().setCredentialsProvider(credentialsProvider).build();

		try (SpeechClient speech = SpeechClient.create(settings)) {

			// Configure remote file request for Linear16
			RecognitionConfig config =
				RecognitionConfig.newBuilder()
					.setEncoding(RecognitionConfig.AudioEncoding.valueOf(analyze.getEncoding()))
					.setLanguageCode(analyze.getLanguageCode())
					.setSampleRateHertz(analyze.getSampleRateHertz())
					.setEnableWordTimeOffsets(true)
					.setEnableAutomaticPunctuation(true)
					.build();
			RecognitionAudio audio = RecognitionAudio.newBuilder().setUri(analyze.getAudio()).build();

			// Use non-blocking call for getting file transcription
			OperationFuture<LongRunningRecognizeResponse, LongRunningRecognizeMetadata> response =
				speech.longRunningRecognizeAsync(config, audio);
			while (!response.isDone()) {
				/**
				 * Wait Google response, as a future.
				 */
				Thread.sleep(TIME_LOOP);
			}
			//
			List<SpeechRecognitionResult> results = response.get().getResultsList();
			for (SpeechRecognitionResult result : results) {
				SpeechRecognitionAlternative alternative = result.getAlternativesList().get(0);
				for (WordInfo wordInfo : alternative.getWordsList()) {
					long start = wordInfo.getStartTime().getSeconds();
					long end = wordInfo.getEndTime().getSeconds();
					LOGGER.info("seconds: " + wordInfo.getStartTime().getNanos());
					LOGGER.info("" + wordInfo.getStartTime().getNanos());
					// Register current word
					registerWord(start, end, wordInfo.getWord());
				}
			}
		}
	}



	public static ServiceAccountCredentials getCredentials() throws IOException {
		return ServiceAccountCredentials.fromStream(new FileInputStream(Config.GOOGLE_CREDENTIALS_PATH.stringValue()));
	}
}
