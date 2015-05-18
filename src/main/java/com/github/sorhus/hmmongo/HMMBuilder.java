package com.github.sorhus.hmmongo;

import java.util.List;

public class HMMBuilder {

    private String piPath, APath, BPath;
    private boolean inLog, outLog = false;
    private boolean adjacency = false;

    public HMMBuilder() {}

    public HMMBuilder fromFiles(String piPath, String APath, String BPath) {
        return fromFiles(piPath, APath, BPath, false);
    }

    public HMMBuilder fromFiles(String piPath, String APath, String BPath, boolean log) {
        this.piPath = piPath;
        this.APath = APath;
        this.BPath = BPath;
        this.inLog = log;
        return this;
    }


    public HMMBuilder adjacency() {
        this.adjacency = true;
        return this;
    }

    public HMMBuilder asLog() {
        this.outLog = true;
        return this;
    }

    public HMM build() {
        double[] pi = null;
        List<Keyed> A = null;
        double[][] B = null;
        return new HMM(pi, A, B, outLog);
    }
}
