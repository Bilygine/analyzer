package com.bilygine.analyzer.analyze;

import com.bilygine.analyzer.analyze.result.ResultBuilder;
import com.bilygine.analyzer.analyze.steps.Step;
import com.bilygine.analyzer.analyze.steps.StepBuilder;
import com.bilygine.analyzer.analyze.steps.StepType;
import com.bilygine.analyzer.analyze.steps.sentiment.GoogleSentimentStep;
import com.bilygine.analyzer.analyze.steps.transcription.FakeTranscriptionStep;
import com.bilygine.analyzer.analyze.steps.transcription.GoogleTranscriptionStep;

import com.bilygine.analyzer.analyze.steps.volume.DefaultVolumeStep;
import com.bilygine.analyzer.configuration.Config;
import com.bilygine.analyzer.io.IO;
import com.bilygine.analyzer.io.Json;
import com.bilygine.analyzer.service.DatabaseService;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.*;

public class DefaultAnalyze implements Analyze {

	/**
	 * Logger
	 */
	private static final Logger LOGGER = LogManager.getLogger(DefaultAnalyze.class);
	// Files for export
	private static final String RESULTS_FILE = "/results.json";
	private static final String ANALYZE_FILE = "/analyze.json";
	//
	private final String id;
	private final String audio;
	private final String languageCode;
	private final String encoding;
	private final int sampleRateHertz;
	private final String source;

	/**
	 * Steps status
	 */
	private List<Step> steps = new LinkedList<>();
	/**
	 * Result
	 */
	private ResultBuilder result = new ResultBuilder();
	/**
	 * Metadata
	 */
	private final String version = "0.1"; // TODO : Make it dynamic with Maven
	/**
	 * Time
	 */
	private TimeMeasure time = new TimeMeasure();

	private static final String FAKE_PREFIX = "fake://";


	public DefaultAnalyze(String id, String sourceId, String audio, String languageCode, String encoding, int sampleRateHertz) throws Exception {
		this.id = id;
		this.source = sourceId;
		this.audio = audio;
		this.languageCode = languageCode;
		this.encoding = encoding;
		this.sampleRateHertz = sampleRateHertz;
		StepBuilder builder = new StepBuilder(this);
		builder.add(StepType.TRANSCRIPTION, this.audio.startsWith(FAKE_PREFIX)
			? FakeTranscriptionStep.class.getName()
			: GoogleTranscriptionStep.class.getName());
		if (Config.SERVICES_VOLUME_DEFAULT.boolValue()) {
			builder.add(StepType.VOLUME, DefaultVolumeStep.class.getName());
		}
		if (Config.SERVICES_SENTIMENT_GOOGLE.boolValue()) {
			builder.add(StepType.SENTIMENT, GoogleSentimentStep.class.getName());
		}
		this.steps = builder.build();
	}

	@Override
	public String getName() {
		return "DefaultAnalyze";
	}

	@Override
	public Status getStatus() {
		return !isDone() ? Status.RUNNING : hasError() ? Status.FAILURE : Status.SUCCEED;
	}

	@Override
	public List<Step> getSteps() {
		return this.steps;
	}

	@Override
	public void run() {
		LOGGER.info("Analyze {} running.", getId());
		time.start();
		/** Execute steps */
		for (Step step : this.steps) {
			try {
				step.setStatus(Status.RUNNING);
				update();
				step.call();
				step.setStatus(Status.SUCCEED);
			} catch (Exception e) {
				step.setError(e.getMessage());
				step.setStatus(Status.FAILURE);
				LOGGER.error("Error during step " + step.getName() + " for analyze " + getId(), e);
			} finally {
				update();
			}
		}
		time.end();
		update();
	}

	/**
	 * ls
	 *
	 * @return
	 */
	public boolean isDone() {
		return this.steps.stream()
			.filter(step -> step.getStatus().equals(Status.SUCCEED) || step.getStatus().equals(Status.FAILURE))
			.count() == this.steps.size();
	}

	public String getSourceId() {
		return this.source;
	}

	/**
	 * Returns true if a step is in 'FAILURE'.
	 *
	 * @return
	 */
	public boolean hasError() {
		return this.steps.stream()
			.filter(step -> step.getStatus().equals(Status.FAILURE))
			.count() > 0;
	}

	public String getId() {
		return this.id;
	}

	@Override
	public void exportResults() {
		LOGGER.info("Exporting...");
		try {
			SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
			String date = simpleDateFormat.format(new Date());
			String directory = Config.EXPORT_BASE.stringValue() + "/" + date + "/" + getId();
			// On local storage
			IO.mkdirs(directory);
			IO.write(directory + RESULTS_FILE, Json.toJson(getResultBuilder().getRoot()));
			IO.write(directory + ANALYZE_FILE, Json.toJson(this));
			// On database
			DatabaseService.getInstance().writeResults(this);
			// TODO: Compute words count
			LOGGER.info("Exported success at: " + directory + " (N/A words)" );
		} catch (Exception e) {
			LOGGER.error("Can't export: ", e);
		}
	}

	@Override
	public String getLanguageCode() {
		return this.languageCode;
	}

	@Override
	public String getEncoding() {
		return this.encoding;
	}

	@Override
	public int getSampleRateHertz() {
		return this.sampleRateHertz;
	}

	public void update() {
		DatabaseService.getInstance().update(getId(), this);
	}

	@Override
	public String getAudio() {
		return this.audio;
	}

	@Override
	public TimeMeasure getTime() {
		return this.time;
	}

	@Override
	public ResultBuilder getResultBuilder() {
		return this.result;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		DefaultAnalyze analyze = (DefaultAnalyze) o;
		return Objects.equals(id, analyze.id) &&
			Objects.equals(audio, analyze.audio);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, audio);
	}

	@Override
	public String toString() {
		return "DefaultAnalyze{" +
			"id='" + id + '\'' +
			", audio='" + audio + '\'' +
			", status='" + getStatus().name() + '\'' +
			", elapsed='" + time.getElapsed() + '\'' +
			'}';
	}
}
