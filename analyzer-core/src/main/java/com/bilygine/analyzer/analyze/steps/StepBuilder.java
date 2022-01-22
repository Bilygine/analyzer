package com.bilygine.analyzer.analyze.steps;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.entity.error.AnalyzerError;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class StepBuilder {

	private List<Class> stepClazz;
	private Set<StepType> types;

	private Analyze analyze;

	public StepBuilder(Analyze analyze) {
		stepClazz = new LinkedList<>();
		types = new HashSet<>();
		this.analyze = analyze;

	}

	public void add(StepType type, String implem) {
		if (type == null || implem == null) {
			return;
		}
		Class implemClazz = type.getImplementation(implem);
		if (implemClazz == null) {
			throw new AnalyzerError("Implementation " + implem + " for step type " + type.name() + " doesn't exist");
		}
		stepClazz.add(implemClazz);
		types.add(type);
	}

	public List<Step> build() throws Exception {
		if (types.contains(StepType.SENTIMENT) && !types.contains(StepType.TRANSCRIPTION)) {
			throw new AnalyzerError("Sentiment analyze need a transcription analyze");
		}
		List<Step> steps = new LinkedList<>();
		for (Class clazz : stepClazz) {
			steps.add((Step) clazz.getDeclaredConstructor(Analyze.class).newInstance(analyze));
		}
		return steps;
	}

}
