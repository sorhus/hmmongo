package com.github.sorhus.hmmongo.thrift;


import com.github.sorhus.hmmongo.viterbi.ObservationDecoder;
import com.github.sorhus.hmmongo.viterbi.PathDecoder;
import com.github.sorhus.hmmongo.viterbi.result.ResultFactory;

public class DNAResultFactory extends ResultFactory<String, String, FullResult> {

    private final FullResult NO_PATH = new FullResult("", "", Double.NEGATIVE_INFINITY);

    public DNAResultFactory(ObservationDecoder<String> encoder, PathDecoder<String> decoder) {
        super(encoder, decoder);
    }

    @Override
    public FullResult apply(int[] input, int[] path, double likelihood) {
        return new FullResult(encoder.apply(input), decoder.apply(path), likelihood);
    }

    @Override
    public FullResult noPath() {
        return NO_PATH;
    }
}
