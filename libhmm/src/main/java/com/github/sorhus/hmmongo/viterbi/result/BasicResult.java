package com.github.sorhus.hmmongo.viterbi.result;

public class BasicResult<I,O> implements Result<I,O> {

    final O path;
    final double likelihood;

    public BasicResult(O path, double likelihood) {
        this.path = path;
        this.likelihood = likelihood;
    }

    @Override
    public String toString() {
        return path.toString() + ":" + Double.toString(likelihood);
    }
}