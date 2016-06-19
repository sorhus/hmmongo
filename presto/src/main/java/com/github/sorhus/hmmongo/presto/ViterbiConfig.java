package com.github.sorhus.hmmongo.presto;

import java.util.Map;

public class ViterbiConfig {

    private String pi;
    private String A;
    private String B;
    private int T;

    public ViterbiConfig(Map<String, String> config) {

        // required
        pi = config.get("pi.location");
        A = config.get("A.location");
        B = config.get("B.location");

        // optional
        T = Integer.parseInt(config.getOrDefault("T", "100"));
    }

    public String getPi() {
        return pi;
    }

    public String getA() {
        return A;
    }

    public String getB() {
        return B;
    }

    public int getT() {
        return T;
    }

}
