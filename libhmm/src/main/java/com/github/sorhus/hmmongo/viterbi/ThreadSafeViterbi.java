package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.viterbi.result.Result;

public class ThreadSafeViterbi<I,R extends Result> implements Viterbi<I,R> {

    private final Viterbi<I,R> impl;

    public ThreadSafeViterbi(Viterbi<I,R> impl) {
        this.impl = impl;
    }

    @Override
    public synchronized R apply(I i) {
        return impl.apply(i);
    }
}
