package com.bilygine.analyzer.analyze.steps.transcription;

import com.bilygine.analyzer.analyze.Analyze;
import com.bilygine.analyzer.analyze.steps.Step;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;

public abstract class TranscriptionStep extends Step {

    private static final Logger LOGGER = LogManager.getLogger(TranscriptionStep.class);
    //

    public TranscriptionStep(Analyze analyze, String name) {
        super(analyze, name);
    }

    /** Entry point for all transcript implementation */
    public abstract void transcript() throws IOException, Exception;

    /**
     * Allow a transcript step to register a word into the current analyze ResultBuilder
     *
     * @param start
     * @param end
     * @param word
     */
    public void registerWord(long start, long end, String word) {
        analyze.getResultBuilder().registerWord(Math.round(start), word, start, end);
    }

    /**
     *
     * @return List of ResultColumns
     */
    @Override
    public void process() throws Exception {
        /** Execute transcription **/
        LOGGER.info("Transcription started");
        this.transcript();
        LOGGER.info("Transcription finished");
    }
}
