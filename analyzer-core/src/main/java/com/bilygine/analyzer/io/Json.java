package com.bilygine.analyzer.io;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.DefaultAnalyze;
import com.bilygine.analyzer.analyze.result.Result;
import com.bilygine.analyzer.analyze.steps.Step;
import com.bilygine.analyzer.entity.json.serializer.AnalyzeSerializer;
import com.bilygine.analyzer.entity.json.serializer.ResultSerializer;
import com.bilygine.analyzer.entity.json.serializer.StepSerializer;
import com.google.gson.GsonBuilder;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;

import java.util.List;

public class Json {

	private static final GsonBuilder gson = new GsonBuilder();
	private static Gson parser;

	static {
		registerTypeAdapters();
		parser = gson.create();
	}

	public static void registerTypeAdapters() {
		gson.registerTypeAdapter(Analyze.class, new AnalyzeSerializer());
		gson.registerTypeAdapter(Result.class, new ResultSerializer());
		gson.registerTypeAdapter(Step.class, new StepSerializer());
	}

	public static String toJson(Object object) {
		return parser.toJson(object);
	}

	public static <T> T fromJson(String content, Class<T> clazz) {
		return parser.fromJson(content, clazz);
	}

	public static JsonElement toJsonTree(Object o, Class clazz) {
		return parser.toJsonTree(o, clazz);
	}

	public static JsonElement toJsonArray(List objects, Class clazz) {
		JsonArray array = new JsonArray();
		for (Object o : objects) {
			array.add(toJsonTree(o, clazz));
		}
		return array;
	}

}
