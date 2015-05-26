package com.github.sorhus.hmmongo;

import java.util.function.Function;

public interface Viterbi<I, O> extends Function<I, Result<O>> {}