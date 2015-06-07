package com.github.sorhus.hmmongo.viterbi.result;

import com.github.sorhus.hmmongo.viterbi.ObservationDecoder;
import com.github.sorhus.hmmongo.viterbi.PathDecoder;

public class FullResultFactory<I,O> extends ResultFactory<I,O> {

    public FullResultFactory(ObservationDecoder<I> encoder, PathDecoder<O> decoder) {
        super(encoder, decoder);
    }

    @Override
    public Result<I, O> apply(int[] input, int[] path, double likelihood) {
        return new FullResult<>(encoder.apply(input), decoder.apply(path), likelihood);
    }

}
