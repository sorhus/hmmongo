package com.github.sorhus.hmmongo;

public class ViterbiResult {
    public final int[] path;
    public final double likelihood;

    public ViterbiResult(int[] path, double likelihood) {
        this.path = path;
        this.likelihood = likelihood;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(path[0]);
        for (int i = 1; i < path.length; i++) {
            sb.append(",").append(path[i]);
        }
        sb.append("\t").append(likelihood);
        return sb.toString();
    }
}
