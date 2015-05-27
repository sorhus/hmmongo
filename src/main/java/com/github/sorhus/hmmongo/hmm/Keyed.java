package com.github.sorhus.hmmongo.hmm;

public interface Keyed {
    Double get(int i);
    Iterable<Integer> keys();
    void put(int j, double v);
}
