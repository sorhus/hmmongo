package com.github.sorhus.hmmongo;

import java.util.ArrayList;
import java.util.Iterator;

public class KeyedList extends ArrayList<Double> implements Keyed {

    public KeyedList(int n) {
        super(n);
    }

    @Override
    public Iterable<Integer> keys() {
        return () -> new Iterator<Integer>() {
            int i = 0;
            @Override
            public boolean hasNext() {
                return i < size();
            }
            @Override
            public Integer next() {
                return i++;
            }
        };
    }

    @Override
    public void put(int j, double v) {
        super.add(j, v);
    }
}
