package com.bilygine.analyzer.analyze.steps.volume;

import com.bilygine.analyzer.analyze.Analyze;

public class DefaultVolumeStep extends VolumeStep {

	public DefaultVolumeStep(Analyze analyze) {
		super(analyze, DefaultVolumeStep.class.getName());
	}

	@Override
	public void process() throws Exception {

	}

	@Override
	public String getDescription() {
		return null;
	}
}
