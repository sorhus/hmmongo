package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.hmm.HMM;

public class ViterbiBuilder {

    final Viterbi<int[], int[]> impl;

    public ViterbiBuilder(HMM hmm, int T) {
        this(hmm, T, false);
    }

    public ViterbiBuilder(HMM hmm, int T, boolean experiment) {
        this.impl = experiment ? new MaybeFasterViterbiImpl(hmm, T) : new ViterbiImpl(hmm, T);
    }

    public Viterbi<int[], int[]> build() {
        return impl;
    }

    public  <I, O> Viterbi<I, O> withEncoderDecoder(Encoder<I> encoder, Decoder<O> decoder) {
        return new ViterbiDecoder<>(new ViterbiEncoder<>(encoder), decoder);
    }

    public <I> Viterbi<I, int[]> withEncoder(Encoder<I> encoder) {
        return withEncoderDecoder(encoder, t -> t);
    }

    public <O> Viterbi<int[], O> withDecoder(Decoder<O> decoder) {
        return withEncoderDecoder(t -> t, decoder);
    }

    class ViterbiDecoder<I, O> implements Viterbi<I, O> {

        private final Viterbi<I, int[]> impl;
        private final Decoder<O> decoder;

        public ViterbiDecoder(Viterbi<I, int[]> impl, Decoder<O> decoder) {
            this.impl = impl;
            this.decoder = decoder;
        }

        @Override
        public Result<O> apply(I input) {
            Result<int[]> result = impl.apply(input);
            return new Result<>(decoder.apply(result.path), result.likelihood);
        }
    }

    class ViterbiEncoder<I> implements Viterbi<I, int[]> {

        private final Encoder<I> encoder;

        public ViterbiEncoder(Encoder<I> encoder) {
            this.encoder = encoder;
        }

        @Override
        public Result<int[]> apply(I input) {
            return impl.apply(encoder.apply(input));
        }
    }

}