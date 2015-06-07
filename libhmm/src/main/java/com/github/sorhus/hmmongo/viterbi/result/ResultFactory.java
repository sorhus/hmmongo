package com.github.sorhus.hmmongo.viterbi.result;

import com.github.sorhus.hmmongo.viterbi.ObservationDecoder;
import com.github.sorhus.hmmongo.viterbi.PathDecoder;

public abstract class ResultFactory<I,O,R extends Result<I,O>> {

    protected final ObservationDecoder<I> encoder;
    protected final PathDecoder<O> decoder;

    public ResultFactory(ObservationDecoder<I> encoder, PathDecoder<O> decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
    }

    public abstract R apply(int[] input, int[] path, double likelihood);
    public abstract R noPath();
}
