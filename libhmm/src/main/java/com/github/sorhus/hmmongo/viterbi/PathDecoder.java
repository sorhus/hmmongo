package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

/**
 *
 * @param <O> the type of an output sequence
 */
public interface PathDecoder<O> extends Function<int[], O> {}
