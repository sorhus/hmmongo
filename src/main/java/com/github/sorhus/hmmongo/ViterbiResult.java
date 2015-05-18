package com.github.sorhus.hmmongo;

public class ViterbiResult {
    public final int[] path;
    public final double likelihood;

    public ViterbiResult(int[] path, double likelihood) {
        this.path = path;
        this.likelihood = likelihood;
    }
}
