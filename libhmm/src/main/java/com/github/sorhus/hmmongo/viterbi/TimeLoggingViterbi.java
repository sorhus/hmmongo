package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.viterbi.result.Result;

import java.util.concurrent.TimeUnit;

class TimeLoggingViterbi<I,R extends Result> implements Viterbi<I,R> {

    private final Viterbi<I,R> impl;

    TimeLoggingViterbi(Viterbi<I, R> impl) {
        this.impl = impl;
    }

    @Override
    public R apply(I input) {
        long t = System.nanoTime();
        R result = impl.apply(input);
        System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - t));
        return result;
    }
}
