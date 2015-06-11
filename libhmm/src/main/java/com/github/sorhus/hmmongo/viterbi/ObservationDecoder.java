package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

/**
 *
 * @param <I> the type of an observation sequence
 */
public interface ObservationDecoder<I> extends Function<int[], I> {}
