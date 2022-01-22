package com.bilygine.analyzer.analyze.steps.preparation;


import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.steps.Step;

public abstract class PreparationStep extends Step {

	public PreparationStep(Analyze analyze, String name) {
		super(analyze, name);
		this.analyze = analyze;
	}
}
