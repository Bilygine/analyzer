package com.bilygine.analyzer.entity.error;

public class AnalyzerError extends RuntimeException {

	public AnalyzerError() {
	}

	public AnalyzerError(String message) {
		super(message);
	}

}