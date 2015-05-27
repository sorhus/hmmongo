package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

public interface Viterbi<I, O> extends Function<I, Result<O>> {}