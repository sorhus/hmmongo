package com.github.sorhus.hmmongo.viterbi.result;

public class FullResult<I,O> extends BasicResult<O> {

    final I input;

    public FullResult(I input, O path, double likelihood) {
        super(path, likelihood);
        this.input = input;
    }

    @Override
    public String toString() {
        return input.toString() + ":" + super.toString();
    }
}
