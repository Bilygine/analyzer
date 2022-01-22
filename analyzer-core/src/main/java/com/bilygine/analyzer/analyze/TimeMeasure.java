package com.bilygine.analyzer.analyze;

public class TimeMeasure {

    /* Time */
    private long start = -1;
    private long end = -1;

    public void start() {
        this.start = System.currentTimeMillis();
    }

    public void end() {
        this.end = System.currentTimeMillis();
    }

    public long getElapsed() {

        if (start == -1) {
            return 0;
        }

        return this.start == -1 ? 0  // Not started
                : this.end == -1 ? System.currentTimeMillis() - this.start // In progress
                : this.end - this.start; // Finished
    }

    public long getStartedAt() {
        return this.start;
    }

    public long getEndAt() {
        return this.end;
    }

}
