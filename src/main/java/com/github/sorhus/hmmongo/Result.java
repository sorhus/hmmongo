package com.github.sorhus.hmmongo;

public class Result<O> {

    final O path;
    final double likelihood;

    public Result(O path, double likelihood) {
        this.path = path;
        this.likelihood = likelihood;
    }

    final static Result<int[]> NO_PATH = new Result<>(null, Double.NaN);
}