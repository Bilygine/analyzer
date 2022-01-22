package com.bilygine.analyzer.analyze;

public enum Status {


    WAITING,
    RUNNING, // When did not complete the steps.
    SUCCEED, // When all step has succeed.
    FAILURE, // When at least one step has not worked.
    ABORTED;

    public String formatted() {
        return this.name().toLowerCase();
    }
}
