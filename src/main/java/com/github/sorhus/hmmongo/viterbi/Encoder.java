package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

public interface Encoder<I> extends Function<I, int[]> {}
