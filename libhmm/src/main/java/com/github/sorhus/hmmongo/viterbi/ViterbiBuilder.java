//package com.github.sorhus.hmmongo.viterbi;
//
//import com.github.sorhus.hmmongo.hmm.HMM;
//import com.github.sorhus.hmmongo.viterbi.result.Result;
//import com.github.sorhus.hmmongo.viterbi.result.ResultFactory;
//
//import java.lang.reflect.Constructor;
//
///**
// *
// * @param <I>
// * @param <O>
// * @param <R>
// */
//public class ViterbiBuilder<I,O,R extends Result> {
//
//    private HMM hmm;
//    private int T;
//    private boolean threadSafe;
//    private ObservationEncoder<I> observationEncoder;
//    private ObservationDecoder<I> observationDecoder;
//    private PathDecoder<O> pathDecoder;
//    private String resultFactoryClass;
//
//    private boolean timeLogging;
//
//    public Viterbi<I,R> build()  {
//        try {
//            @SuppressWarnings("unchecked") // Will throw exception on
//            Class<ResultFactory<I,O,R>> clazz = (Class<ResultFactory<I,O,R>>) Class.forName(resultFactoryClass);
//            Constructor<ResultFactory<I,O,R>> cons = clazz.getConstructor(ObservationDecoder.class, PathDecoder.class);
//            ResultFactory<I,O,R> resultFactory = cons.newInstance(observationDecoder, pathDecoder);
//            Viterbi<I,R> viterbi = new ViterbiImpl<>(hmm, T, observationEncoder, resultFactory);
//            viterbi = timeLogging ? new TimeLoggingViterbi(viterbi) : viterbi;
//            viterbi = threadSafe ? new ThreadSafeViterbi<>(viterbi) : viterbi;
//            return viterbi;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public ViterbiBuilder<I,O,R> withHMM(HMM hmm) {
//        this.hmm = hmm;
//        return this;
//    }
//
//    public ViterbiBuilder<I,O,R> withMaxObservationLength(int T) {
//        this.T = T;
//        return this;
//    }
//
//    public ViterbiBuilder<I,O,R> withThreadSafety() {
//        threadSafe = true;
//        return this;
//    }
//
//    public ViterbiBuilder<I,O,R> withObservationEncoder(ObservationEncoder<I> observationEncoder) {
//        this.observationEncoder = observationEncoder;
//        return this;
//    }
//
//    public ViterbiBuilder<I,O,R> withObservationDecoder(ObservationDecoder<I> observationDecoder) {
//        this.observationDecoder = observationDecoder;
//        return this;
//    }
//
//    public ViterbiBuilder<I,O,R> withPathDecoder(PathDecoder<O> pathDecoder) {
//        this.pathDecoder = pathDecoder;
//        return this;
//    }
//
//    public ViterbiBuilder<I,O,R> withResultFactoryClass(String resultFactoryClass) {
//        this.resultFactoryClass = resultFactoryClass;
//        return this;
//    }
//
//    public ViterbiBuilder<I,O,R> withTimeLogging() {
//        this.timeLogging = true;
//        return this;
//    }
//}