package com.github.sorhus.hmmongo.viterbi.result;

public class BasicResult<O> implements Result {

    public final O path;
    public final double likelihood;

    public BasicResult(O path, double likelihood) {
        this.path = path;
        this.likelihood = likelihood;
    }

    @Override
    public String toString() {
        return path.toString() + ":" + Double.toString(likelihood);
    }
}