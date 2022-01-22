package com.bilygine.analyzer.entity.json.model;

import com.bilygine.analyzer.analyze.steps.Step;

public class StepModel {

	private String name;
	private String description;
	private String error;
	private String status;
	private long start;
	private long end;

	public StepModel(Step step) {
		this.name = step.getName();
		this.description = step.getDescription();
		this.error = step.getError();
		this.status = step.getStatus().name();
		this.start = step.getTime().getStartedAt();
		this.end = step.getTime().getEndAt();
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getError() {
		return error;
	}

	public String getStatus() {
		return status;
	}

	public long getStart() {
		return start;
	}

	public long getEnd() {
		return end;
	}
}
