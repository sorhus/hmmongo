package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.viterbi.result.Result;

import java.util.function.Function;

public interface Viterbi<I,R extends Result> extends Function<I,R> {}