package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.viterbi.result.Result;

import java.util.function.Function;

public interface Viterbi<I,O,R extends Result<I,O>> extends Function<I,R> {}