package com.bilygine.analyzer.service;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.entity.error.AnalyzerError;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

public class AnalyzeService {

	private static final Logger LOGGER = LogManager.getLogger(AnalyzeService.class);
	private static final Map<String, Analyze> analyzes = new HashMap<>();
	private static final int MAX_CONCURRENT_ANALYZE = 10;
	private static final Semaphore semaphore = new Semaphore(MAX_CONCURRENT_ANALYZE);
	private static final ExecutorService executor = Executors.newFixedThreadPool(MAX_CONCURRENT_ANALYZE);

	public void add(Analyze analyze) throws AnalyzerError {
		LOGGER.info("Analyze {} added", analyze.getId());
		analyzes.put(analyze.getId(), analyze);
		DatabaseService.getInstance().create(analyze);
	}

	public String execute(Analyze analyze) throws AnalyzerError {
		if (!semaphore.tryAcquire(1)) {
			throw new AnalyzerError("Analyze refused. Too much analysis in concurrence (" + MAX_CONCURRENT_ANALYZE + ")");
		}
		add(analyze);
		executor.submit(() -> {
			analyze.run();
			analyze.exportResults();
			semaphore.release();
		});
		return analyze.getId();
	}

	public List<Analyze> getAnalyzes() {
		return new ArrayList(analyzes.values());
	}

	public Analyze get(String id) {
		if (!analyzes.containsKey(id)) {
			throw new AnalyzerError("No analyze found with this id: " + id);
		}
		return analyzes.get(id);
	}
}
