package com.bilygine.analyzer.configuration;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

/**
 *
 * @author esteban
 */
public enum Config {

	/**
	 * Configuration keys in config file
	 */

	// Analyzer
	HOST("127.0.0.1"),
	PORT("8082"),

	// Google Cloud Platform
	GOOGLE_CREDENTIALS_PATH("google_credentials.json"),

	// File System
	FS_TYPE("LOCAL"),

	// Results export
	EXPORT_BASE("results"),
	EXPORT_TYPE("JSON"),

	// Database

	DATABASE(""),
	DATABASE_HOST("127.0.0.1"),
	DATABASE_PORT("28015"),
	DATABASE_DBNAME("dev"),
	DATABASE_USER("admin"),
	DATABASE_PASSWORD(""),

	// Tables
	DATABASE_TABLE_ANALYZES("analyzes"),
	DATABASE_TABLE_RESULTS("results"),
	DATABASE_TABLE_SOURCES("sources"),

	/* SERVICES */
	// Sentiment
	SERVICES_SENTIMENT_GOOGLE("false"),
	SERVICES_SENTIMENT_GOOGLE_HOST("127.0.0.1"),
	SERVICES_SENTIMENT_GOOGLE_PORT("5002"),
	// Volume
	SERVICES_VOLUME_DEFAULT("false"),
	SERVICES_VOLUME_DEFAULT_HOST("127.0.0.1"),
	SERVICES_VOLUME_DEFAULT_PORT("5003");


	private static final Logger LOGGER = LogManager.getLogger(Config.class);
	private static final String CONFIG_FILE = "analyzer.conf";
	private String value;

	public void setValue(String value) {
		this.value = value;
	}

	Config() {
		this(null);
	}

	Config(String defaultValue) {
		this.value = defaultValue;
	}

	public int intValue() {
		return Integer.parseInt(value);
	}

	public String stringValue() {
		return this.value;
	}


	public boolean boolValue() {
		return value.equalsIgnoreCase("true");
	}

	public String key() {
		return this.name().toLowerCase().replaceAll("_", ".");
	}

	public static void readConfig() throws IOException {
		Properties prop = new Properties();
		InputStream input = Config.class.getResourceAsStream("/" + CONFIG_FILE);
		if (input != null) {
			prop.load(input);
			Arrays.stream(Config.values())
					.filter(cv -> prop.getProperty(cv.key()) != null)
					.forEach(cv -> cv.setValue(prop.getProperty(cv.key())));
			Arrays.stream(Config.values())
				.filter(cv -> System.getenv(cv.name()) != null)
				.forEach(cv -> cv.setValue(System.getenv(cv.name())));
			Arrays.stream(Config.values()).forEach(v -> LOGGER.info(v.key() + ": " + v.value));
			LOGGER.info("Configuration loaded");
		} else {
			LOGGER.warn("Property file '" + CONFIG_FILE + "' not found in classpath.");
		}
	}

}
