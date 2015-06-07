package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

public interface PathDecoder<O> extends Function<int[], O> {}
