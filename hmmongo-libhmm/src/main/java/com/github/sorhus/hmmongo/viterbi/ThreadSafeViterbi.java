package com.github.sorhus.hmmongo.viterbi;

public class ThreadSafeViterbi<I, O> implements Viterbi<I, O> {

    private final Viterbi<I, O> impl;

    public ThreadSafeViterbi(Viterbi<I, O> impl) {
        this.impl = impl;
    }

    @Override
    public synchronized Result<O> apply(I i) {
        return impl.apply(i);
    }
}
