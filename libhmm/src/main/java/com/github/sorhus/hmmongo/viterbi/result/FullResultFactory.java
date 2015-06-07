package com.github.sorhus.hmmongo.viterbi.result;

import com.github.sorhus.hmmongo.viterbi.ObservationDecoder;
import com.github.sorhus.hmmongo.viterbi.PathDecoder;

public class FullResultFactory<I,O> extends ResultFactory<I,O,FullResult<I,O>> {

    private FullResult<I,O> NO_PATH;

    public FullResultFactory(ObservationDecoder<I> encoder, PathDecoder<O> decoder) {
        super(encoder, decoder);
    }

    @Override
    public FullResult<I,O> apply(int[] input, int[] path, double likelihood) {
        return new FullResult<>(encoder.apply(input), decoder.apply(path), likelihood);
    }

    @Override
    public FullResult<I, O> noPath() {
        if(NO_PATH == null) {
            NO_PATH = new FullResult<I,O>(encoder.apply(new int[] {}), decoder.apply(new int[] {}), Double.NEGATIVE_INFINITY) {};
        }
        return NO_PATH;
    }

}

