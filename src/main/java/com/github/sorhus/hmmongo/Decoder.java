package com.github.sorhus.hmmongo;

import java.util.function.Function;

public interface Decoder<O> extends Function<int[], O> {}
