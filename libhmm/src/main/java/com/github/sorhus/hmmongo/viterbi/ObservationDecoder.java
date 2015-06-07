package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

public interface ObservationDecoder<I> extends Function<int[], I> {}
