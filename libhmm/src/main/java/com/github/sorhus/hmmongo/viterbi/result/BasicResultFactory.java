package com.github.sorhus.hmmongo.viterbi.result;

import com.github.sorhus.hmmongo.viterbi.ObservationDecoder;
import com.github.sorhus.hmmongo.viterbi.PathDecoder;

public class BasicResultFactory<I,O> extends ResultFactory<I,O,BasicResult<O>> {

    private BasicResult<O> NO_PATH;

    public BasicResultFactory(ObservationDecoder<I> encoder, PathDecoder<O> decoder) {
        super(encoder, decoder);
    }

    @Override
    public BasicResult<O> apply(int[] input, int[] path, double likelihood) {
        return new BasicResult<>(decoder.apply(path), likelihood);
    }

    @Override
    public BasicResult<O> noPath() {
        if(NO_PATH == null) {
            NO_PATH = new BasicResult<O>(decoder.apply(new int[] {}), Double.NEGATIVE_INFINITY) {};
        }
        return NO_PATH;
    }
}
