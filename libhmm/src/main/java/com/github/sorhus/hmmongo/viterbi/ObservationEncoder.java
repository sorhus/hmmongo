package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

public interface ObservationEncoder<I> extends Function<I, int[]> {}
