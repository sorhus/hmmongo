package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.viterbi.result.Result;

class ThreadSafeViterbi<I,R extends Result> implements Viterbi<I,R> {

    private final Viterbi<I,R> impl;

    ThreadSafeViterbi(Viterbi<I,R> impl) {
        this.impl = impl;
    }

    @Override
    public synchronized R apply(I i) {
        return impl.apply(i);
    }
}
