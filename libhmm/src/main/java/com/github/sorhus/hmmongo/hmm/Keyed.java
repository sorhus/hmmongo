package com.github.sorhus.hmmongo.hmm;

/**
 * Interface used for <code>A</code> in <code>HMM</code>.
 * Implementations should provide <code>get(i)</code> and <code>put(i,j)</code> in O(1).
 * <code>keys()</code> should return all <code>i</code> in predictable order for stable results.
 */
public interface Keyed {
    Double get(int i);
    Iterable<Integer> keys();
    void put(int j, double v);
}
