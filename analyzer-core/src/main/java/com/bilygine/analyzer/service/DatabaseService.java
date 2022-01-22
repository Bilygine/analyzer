package com.bilygine.analyzer.service;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.result.ResultBuilder;
import com.bilygine.analyzer.configuration.Config;
import com.bilygine.analyzer.entity.error.AnalyzerError;
import com.bilygine.analyzer.entity.json.model.StepModel;

import com.rethinkdb.RethinkDB;
import com.rethinkdb.gen.ast.Table;
import com.rethinkdb.model.MapObject;
import com.rethinkdb.net.Connection;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.stream.Collectors;

public class DatabaseService {

	private static final Logger LOGGER = LogManager.getLogger(DatabaseService.class);

	private static DatabaseService INSTANCE;
	private final RethinkDB r = RethinkDB.r;
	private Connection conn;

	private DatabaseService() {
		LOGGER.info("Database connection...");
		conn = r.connection()
			.hostname(Config.DATABASE_HOST.stringValue())
			.port(Config.DATABASE_PORT.intValue())
			.user(Config.DATABASE_USER.stringValue(), Config.DATABASE_PASSWORD.stringValue())
			.db(Config.DATABASE_DBNAME.stringValue())
			.connect();
		LOGGER.info("Database connection successful");
	}

	public void create(Analyze analyze) {
		MapObject o = marshall(analyze);
		analyzesTable().insert(o).run(conn);
	}

	public void update(String id, Analyze analyze) {
		MapObject o = marshall(analyze);
		analyzesTable().get(id).update(o).run(conn);
	}

	public void writeResults(Analyze analyze) {
		//resultsTable().insert(o).run(conn);
		ResultBuilder builder = analyze.getResultBuilder();
		builder.put("id", analyze.getId());
		resultsTable().insert(builder.getRoot()).run(conn);
	}

	public String getPathFromSourceId(String sourceId) {
		if (sourcesTable().get(sourceId).run(conn) == null) {
			throw new AnalyzerError(String.format("Source %s was not found. Can't retrieve audio path.", sourceId));
		}
		return
			sourcesTable()
			.get(sourceId)
			.getField("download")
			.run(conn);
	}

	/**
	 * Marshall an analyze to JSON.
	 * We keep only useful properties.
	 *
	 * @param analyze
	 * @return
	 */
	private MapObject marshall(Analyze analyze) {
		MapObject o = r.hashMap("id", analyze.getId());
		o.with("source_id", analyze.getSourceId());
		o.with("status", analyze.getStatus().name());
		o.with("time", analyze.getTime());
		o.with("steps",
			analyze.getSteps().stream().map(
				step -> new StepModel(step)
			).collect(Collectors.toList()));
		return o;
	}

	private Table analyzesTable() {
		return getTable(Config.DATABASE_TABLE_ANALYZES.stringValue());
	}

	private Table resultsTable() {
		return getTable(Config.DATABASE_TABLE_RESULTS.stringValue());
	}

	private Table sourcesTable() {
		return getTable(Config.DATABASE_TABLE_SOURCES.stringValue());
	}

	private Table getTable(String table) {
		if (!tableExists(table)) {
			r.tableCreate(table).run(conn);
			LOGGER.info("Table '" + table + "' created !");
		}
		return r.table(table);
	}

	private boolean tableExists(String table) {
		return ((ArrayList) r.tableList().run(conn)).contains(table);
	}

	public static DatabaseService getInstance() {
			if (INSTANCE == null) {
				INSTANCE = new DatabaseService();
			}
			return INSTANCE;
	}

}
