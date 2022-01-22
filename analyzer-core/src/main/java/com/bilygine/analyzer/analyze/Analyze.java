package com.bilygine.analyzer.analyze;

import com.bilygine.analyzer.analyze.result.Result;
import com.bilygine.analyzer.analyze.result.ResultBuilder;
import com.bilygine.analyzer.analyze.steps.Step;

import java.util.List;

public interface Analyze extends Runnable {

    /**
     * @return the name of the analyze
     */
    String getName();

    /**
     * @return the status of the analyze
     */
    Status getStatus();

    /**
     * set steps to analyze
     */
    List<Step> getSteps();

    /**
     * @return audio path
     */
    String getAudio();

    /**
     * Measure elapsed time
     * @return
     */
    TimeMeasure getTime();

    /**
     * @return result
     */
    ResultBuilder getResultBuilder();

    /**
     * UniqueID
     */
    String getId();

    /**
     * Export results
     */
    void exportResults();

    String getLanguageCode();

    String getEncoding();

    int getSampleRateHertz();

    String getSourceId();

}
