package com.bilygine.analyzer.analyze.steps;

import com.bilygine.analyzer.analyze.steps.sentiment.GoogleSentimentStep;
import com.bilygine.analyzer.analyze.steps.transcription.FakeTranscriptionStep;
import com.bilygine.analyzer.analyze.steps.transcription.GoogleTranscriptionStep;
import com.bilygine.analyzer.analyze.steps.volume.DefaultVolumeStep;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;
import java.util.Optional;

public enum StepType {

	TRANSCRIPTION(GoogleTranscriptionStep.class, FakeTranscriptionStep.class),
	SENTIMENT(GoogleSentimentStep.class),
	VOLUME(DefaultVolumeStep.class);

	private static final Logger LOGGER = LogManager.getLogger(StepType.class);

	private Class<? extends Step>[] implementations;

	StepType(Class<? extends Step> ...clazz) {
		this.implementations = clazz;
	}

	public Class[] getImplementations() {
		return this.implementations;
	}

	public Class getImplementation(String name) {
		Optional<Class> clazzOpt = Arrays.stream(this.getImplementations()).filter(clazz -> {
			try {
				return name.equalsIgnoreCase(clazz.getName());
			} catch (Exception e) {
				LOGGER.error("Error while retrieve implementation", e);
			}
			return false;
		}).findFirst();
		return clazzOpt.isPresent() ? clazzOpt.get() : null;
	}
}
