package com.bilygine.analyzer.analyze.steps.volume;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.result.ResultBuilder;
import com.bilygine.analyzer.analyze.steps.Step;

public abstract class VolumeStep extends Step {

	public VolumeStep(Analyze analyze, String name) {
		super(analyze, name);
	}

	/**
	 * Allow volume analyze implementation to register a volume for a given second.
	 *
	 * @param secondTime
	 * @param value
	 */
	public void registerVolumeAt(int secondTime, long value) {
		analyze.getResultBuilder().registerToTimeline(secondTime, ResultBuilder.VOLUME, value);
	}

}
