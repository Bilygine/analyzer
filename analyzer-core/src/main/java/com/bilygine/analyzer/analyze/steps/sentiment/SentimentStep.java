package com.bilygine.analyzer.analyze.steps.sentiment;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.result.ResultBuilder;
import com.bilygine.analyzer.analyze.steps.Step;

public abstract class SentimentStep extends Step {

	public SentimentStep(Analyze analyze, String name) {
		super(analyze, name);
		this.analyze = analyze;
	}

	public Analyze getAnalyze() {
		return this.analyze;
	}

	public String getText() {
		return (String) analyze.getResultBuilder()
			.get(ResultBuilder.TRANSCRIPT)
			.getRoot().get(ResultBuilder.TEXT);
	}

}
