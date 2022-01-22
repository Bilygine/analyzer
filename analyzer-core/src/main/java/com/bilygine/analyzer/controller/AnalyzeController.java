package com.bilygine.analyzer.controller;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.DefaultAnalyze;
import com.bilygine.analyzer.entity.error.AnalyzerError;
import com.bilygine.analyzer.entity.json.input.CreateAnalyzeInput;
import com.bilygine.analyzer.service.AnalyzeService;
import com.bilygine.analyzer.analyze.result.Result;
import com.bilygine.analyzer.io.Json;

import com.bilygine.analyzer.service.DatabaseService;
import org.apache.commons.lang3.RandomStringUtils;
import spark.Request;
import spark.Spark;

import java.util.List;
import java.util.Map;


public class AnalyzeController implements Controller {

	private final AnalyzeService analyzeService = new AnalyzeService();
	private final int ID_LENGTH = 6;

	public AnalyzeController() {}

	@Override
	public void register() {
		Spark.get("/analyzer", (req, res) -> list(), Json::toJson);
		Spark.get("/analyzer/:id", (req, res) -> get(req.params("id")));
		Spark.get("/analyzer/result/:id", (req, res) -> getResult(req.params("id")), Json::toJson);
		Spark.post("/analyzer/execute", (req, res) -> execute(req), Json::toJson);
	}

	private String execute(Request request) throws Exception {
		String body = request.body();
		CreateAnalyzeInput input = Json.fromJson(body, CreateAnalyzeInput.class);
		String id = RandomStringUtils.randomAlphanumeric(ID_LENGTH);
		String sourceId = input.getSourceId();
		String audio = DatabaseService.getInstance().getPathFromSourceId(sourceId);
		return analyzeService.execute(new DefaultAnalyze(id, sourceId, audio, input.getLanguageCode(), input.getEncoding(), input.getSampleRateHertz()));
	}

	private Map<String, Object> getResult(String analyzeId) {
		return analyzeService.get(analyzeId).getResultBuilder().getRoot();
	}

	public List<Analyze> list() {
		return analyzeService.getAnalyzes();
	}

	private String get(String id) {
		return Json.toJson(analyzeService.get(id));
	}
}
