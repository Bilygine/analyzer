package com.bilygine.analyzer;

import com.bilygine.analyzer.configuration.Config;
import com.bilygine.analyzer.controller.AnalyzeController;
import com.bilygine.analyzer.controller.Controller;
import com.bilygine.analyzer.entity.error.AnalyzerError;
import com.bilygine.analyzer.entity.error.ForbiddenException;
import com.bilygine.analyzer.entity.error.NotFoundException;
import com.bilygine.analyzer.io.IO;
import com.bilygine.analyzer.io.Json;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import spark.Response;
import spark.Spark;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Analyzer entry class
 *
 * @author esteban
 */
public class Analyzer {

	private static final Logger LOGGER = LogManager.getLogger(Analyzer.class);
	private static final List<Controller> controllers = Arrays.asList(new AnalyzeController());

	public static void main(String args[]) {
		// Retrieve configuration
		try {
			Config.readConfig();
		} catch (IOException e) {
			LOGGER.error("Error while read configuration.", e);
		}
		// Initialize file system
		IO.init();
		/** Start web server */
		run(Config.HOST.stringValue(), Config.PORT.intValue());
	}

	public static void run(String host, int port) {
		Spark.ipAddress(host);
		Spark.port(port);
		Spark.initExceptionHandler(exception -> {
			LOGGER.error("An error occurred during launch. Server will stop.", exception);
			System.exit(-1);
		});
		options();
		before();
		exceptions();
		Spark.path("/api", () -> controllers.stream().forEach(Controller::register));
		LOGGER.info("Starting Analyzer on port " + Spark.port());
	}

	private static void options() {
		// CORS Implementation
		Spark.options("/*", (request, response) -> {
			String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
			if (accessControlRequestHeaders != null) {
				response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
			}
			String accessControlRequestMethod = request.headers("Access-Control-Request-Method");
			if (accessControlRequestMethod != null) {
				response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
			}
			return "OK";
		});
	}

	private static void before() {
		Spark.before((request, response) -> {
			response.type("application/json; charset=UTF-8");
			response.header("Content-Encoding", "gzip");
			response.header("Access-Control-Allow-Origin", "*");
		});
	}

	private static void exceptions() {
		Spark.exception(NotFoundException.class, ((exception, request, response) -> onError(response, exception, 404)));
		Spark.exception(IllegalArgumentException.class, ((exception, request, response) -> onError(response, exception, 400)));
		Spark.exception(ForbiddenException.class, ((exception, request, response) -> onError(response, exception, 403)));
		Spark.exception(Exception.class, ((exception, request, response) -> onError(response, exception, 500)));
		Spark.notFound((req, res) -> Json.toJson(new AnalyzerError("Route " + req.pathInfo() + " does not exists")));
	}

	private static void onError(Response response, Exception exception, int code) {
		LOGGER.error(exception.getMessage(), exception);
		response.body(Json.toJson(new AnalyzerError(exception.getMessage())));
		response.status(code);
	}
}
