package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.viterbi.result.Result;

public class ThreadSafeViterbi<I,O,R extends Result<I,O>> implements Viterbi<I,O,R> {

    private final Viterbi<I,O,R> impl;

    public ThreadSafeViterbi(Viterbi<I,O,R> impl) {
        this.impl = impl;
    }

    @Override
    public synchronized R apply(I i) {
        return impl.apply(i);
    }
}
