package com.github.sorhus.hmmongo.viterbi;

public class Result<O> {

    final O path;
    final double likelihood;

    public Result(O path, double likelihood) {
        this.path = path;
        this.likelihood = likelihood;
    }

    @Override
    public String toString() {
        return path.toString() + "\t" + Double.toString(likelihood) + "\n";
    }

    final static Result<int[]> NO_PATH = new Result<>(null, Double.NaN);
}