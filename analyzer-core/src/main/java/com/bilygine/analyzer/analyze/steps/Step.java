package com.bilygine.analyzer.analyze.steps;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.Status;
import com.bilygine.analyzer.analyze.TimeMeasure;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;
import java.util.concurrent.Callable;

public abstract class Step implements Callable<Map<String, Object>> {

    private final static Logger LOGGER = LogManager.getLogger(Step.class);

    private String error;
    private String name;
    private TimeMeasure time = new TimeMeasure();
    private Status status = Status.WAITING;
    //
    protected Analyze analyze;

    public Step(Analyze analyze, String name) {
        this.analyze = analyze;
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public Map<String, Object> call() throws Exception {
        try {
            LOGGER.info("Step '{}' is starting.", name);
            setStatus(Status.RUNNING);
            time.start();
            process();
            status = Status.SUCCEED;
        } finally {
            time.end();
            LOGGER.info("Step '{}' finished in {} ms. Status={}", name, time.getElapsed(), status.name());
        }
        return null;
    }

    public void setStatus(Status status) {
        String old = this.status.name();
        this.status = status;
        LOGGER.info("Step '{}': {} => {}", getName(), old, status.name());
    }

    public abstract void process() throws Exception;

    /**
     * @return a simple description for step goal
     */
    public abstract String getDescription();

    public void setError(String error) {
        this.error = error;
    }

    public Status getStatus() {
        return error == null ? this.status : Status.FAILURE;
    }

    public TimeMeasure getTime() {
        return this.time;
    }

    public String getError() {
        return this.error;
    }
}
