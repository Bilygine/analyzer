package com.bilygine.analyzer.entity.json.model;

public class Occurence {
	private String occurence;
	private long timestamp;

	public Occurence(String occurence) {
		this.occurence = occurence;
	}

	public String getOccurence() {
		return this.occurence;
	}

	public long getTimestamp() {
		return this.timestamp;
	}
}
