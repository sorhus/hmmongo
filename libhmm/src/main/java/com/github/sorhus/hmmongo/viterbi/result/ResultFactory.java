package com.github.sorhus.hmmongo.viterbi.result;

import com.github.sorhus.hmmongo.viterbi.ObservationDecoder;
import com.github.sorhus.hmmongo.viterbi.PathDecoder;

public abstract class ResultFactory<I,O> {

    protected final ObservationDecoder<I> encoder;
    protected final PathDecoder<O> decoder;
    public final Result<I,O> NO_PATH;

    public ResultFactory(ObservationDecoder<I> encoder, PathDecoder<O> decoder) {
        this.encoder = encoder;
        this.decoder = decoder;
        this.NO_PATH = new Result<I,O>() {};
    }

    public abstract Result<I,O> apply(int[] input, int[] path, double likelihood);

}
