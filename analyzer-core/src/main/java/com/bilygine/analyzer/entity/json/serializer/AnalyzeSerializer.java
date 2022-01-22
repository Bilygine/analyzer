package com.bilygine.analyzer.entity.json.serializer;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.TimeMeasure;
import com.bilygine.analyzer.analyze.steps.Step;
import com.bilygine.analyzer.io.Json;
import com.google.gson.*;

import java.lang.reflect.Type;

public class AnalyzeSerializer implements JsonSerializer<Analyze> {


	public JsonElement serialize(Analyze analyze, Type type, JsonSerializationContext context) {
		JsonObject root = new JsonObject();
		root.addProperty("id", analyze.getId());
		root.addProperty("status", analyze.getStatus().name());
		root.add("time", Json.toJsonTree(analyze.getTime(), TimeMeasure.class));
		root.add("steps", Json.toJsonArray(analyze.getSteps(), Step.class));
		return root;
	}


}