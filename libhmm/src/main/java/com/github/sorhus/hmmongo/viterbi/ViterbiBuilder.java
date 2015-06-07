package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.viterbi.result.Result;
import com.github.sorhus.hmmongo.viterbi.result.ResultFactory;

import java.lang.reflect.Constructor;

public class ViterbiBuilder<I,O,R extends Result<I,O>> {

    private HMM hmm;
    private int T;
    private boolean threadSafe;
    private ObservationEncoder<I> observationEncoder;
    private ObservationDecoder<I> observationDecoder;
    private PathDecoder<O> pathDecoder;
    private String resultFactoryClass;

    public Viterbi<I,O,R> build()  {
        try {
            @SuppressWarnings("unchecked") // Will throw exception on
            Class<ResultFactory<I,O,R>> clazz = (Class<ResultFactory<I,O,R>>) Class.forName(resultFactoryClass);
            Constructor<ResultFactory<I,O,R>> cons = clazz.getConstructor(ObservationDecoder.class, PathDecoder.class);
            ResultFactory<I,O,R> resultFactory = cons.newInstance(observationDecoder, pathDecoder);
            Viterbi<I,O,R> impl = new ViterbiImpl<>(hmm, T, observationEncoder, resultFactory);
            return threadSafe ? new ThreadSafeViterbi<>(impl) : impl;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public ViterbiBuilder<I,O,R> withHMM(HMM hmm) {
        this.hmm = hmm;
        return this;
    }

    public ViterbiBuilder<I,O,R> withMaxObservationLength(int T) {
        this.T = T;
        return this;
    }

    public ViterbiBuilder<I,O,R> threadSafe() {
        threadSafe = true;
        return this;
    }

    public ViterbiBuilder<I,O,R> withObservationEncoder(ObservationEncoder<I> observationEncoder) {
        this.observationEncoder = observationEncoder;
        return this;
    }

    public ViterbiBuilder<I,O,R> withObservationDecoder(ObservationDecoder<I> observationDecoder) {
        this.observationDecoder = observationDecoder;
        return this;
    }

    public ViterbiBuilder<I,O,R> withPathDecoder(PathDecoder<O> pathDecoder) {
        this.pathDecoder = pathDecoder;
        return this;
    }

    public ViterbiBuilder<I,O,R> withResultFactoryClass(String resultFactoryClass) {
        this.resultFactoryClass = resultFactoryClass;
        return this;
    }
}