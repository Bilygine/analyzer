package com.bilygine.analyzer.entity.json.serializer;

import com.bilygine.analyzer.analyze.TimeMeasure;
import com.bilygine.analyzer.analyze.steps.Step;
import com.bilygine.analyzer.io.Json;
import com.google.gson.*;

import java.lang.reflect.Type;

public class StepSerializer implements JsonSerializer<Step> {

	@Override
	public JsonElement serialize(Step step, Type type, JsonSerializationContext jsonSerializationContext) {
		JsonObject root = new JsonObject();
		root.addProperty("name", step.getName());
		root.addProperty("description",step.getDescription());
		root.addProperty("status", step.getStatus().name());
		root.add("time", Json.toJsonTree(step.getTime(), TimeMeasure.class));
		return root;
	}

}