package com.github.sorhus.hmmongo;

public interface Keyed {
    Double get(int i);
    Iterable<Integer> keys();
    void put(int j, double v);
}
