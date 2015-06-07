package com.github.sorhus.hmmongo.hmm;

import java.util.LinkedHashMap;

public class KeyedMap extends LinkedHashMap<Integer, Double> implements Keyed {

    @Override
    public Double get(int i) {
        return super.get(i);
    }

    @Override
    public Iterable<Integer> keys() {
        return super.keySet();
    }

    @Override
    public void put(int j, double v) {
        super.put(j, v);
    }
}
