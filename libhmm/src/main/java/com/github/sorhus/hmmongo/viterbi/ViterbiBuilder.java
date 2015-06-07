package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.viterbi.result.ResultFactory;

import java.lang.reflect.Constructor;

public class ViterbiBuilder<I,O> {

    private HMM hmm;
    private int T;
    private boolean threadSafe;
    private ObservationEncoder<I> observationEncoder;
    private ObservationDecoder<I> observationDecoder;
    private PathDecoder<O> pathDecoder;
    private String resultFactoryClass;

    public Viterbi<I,O> build()  {
        try {
            @SuppressWarnings("unchecked") // Will throw exception on
            Class<ResultFactory<I, O>> clazz = (Class<ResultFactory<I, O>>) Class.forName(resultFactoryClass);
            Constructor<ResultFactory<I, O>> cons = clazz.getConstructor(ObservationDecoder.class, PathDecoder.class);
            ResultFactory<I, O> resultFactory = cons.newInstance(observationDecoder, pathDecoder);
            Viterbi<I, O> impl = new ViterbiImpl<>(hmm, T, observationEncoder, resultFactory);
            return threadSafe ? new ThreadSafeViterbi<>(impl) : impl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ViterbiBuilder<I,O> withHMM(HMM hmm) {
        this.hmm = hmm;
        return this;
    }

    public ViterbiBuilder<I,O> withMaxObservationLength(int T) {
        this.T = T;
        return this;
    }

    public ViterbiBuilder<I,O> threadSafe() {
        threadSafe = true;
        return this;
    }

    public ViterbiBuilder<I,O> withObservationEncoder(ObservationEncoder<I> observationEncoder) {
        this.observationEncoder = observationEncoder;
        return this;
    }

    public ViterbiBuilder<I,O> withObservationDecoder(ObservationDecoder<I> observationDecoder) {
        this.observationDecoder = observationDecoder;
        return this;
    }

    public ViterbiBuilder<I,O> withPathDecoder(PathDecoder<O> pathDecoder) {
        this.pathDecoder = pathDecoder;
        return this;
    }

    public ViterbiBuilder<I,O> withResultFactoryClass(String resultFactoryClass) {
        this.resultFactoryClass = resultFactoryClass;
        return this;
    }
}