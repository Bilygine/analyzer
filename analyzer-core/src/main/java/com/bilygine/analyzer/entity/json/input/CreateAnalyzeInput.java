package com.bilygine.analyzer.entity.json.input;

/**
 * Model of analyze input
 * @author esteban
 */
public class CreateAnalyzeInput {

	private String source_id;
	private String language_code;
	private int sample_rate_hertz;
	private String encoding;

	public CreateAnalyzeInput() {}

	public String getSourceId() {
		return this.source_id;
	}

	public int getSampleRateHertz() {
		return sample_rate_hertz;
	}

	public String getEncoding() {
		return encoding;
	}

	public String getLanguageCode() {
		return language_code;
	}
}
