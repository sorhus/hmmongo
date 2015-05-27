package com.github.sorhus.hmmongo.viterbi;

import java.util.function.Function;

public interface Decoder<O> extends Function<int[], O> {}
