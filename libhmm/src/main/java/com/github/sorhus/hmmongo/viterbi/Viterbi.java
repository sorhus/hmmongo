package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.viterbi.result.Result;
import com.github.sorhus.hmmongo.viterbi.result.ResultFactory;

import java.lang.reflect.Constructor;
import java.util.function.Function;

/**
 * Defines <code>Viterbi</code> as a function from some sequence type
 * <code>I</code> to some type <code>R</code> that extends <code>Result</code>.
 *
 * @param <I> the type of an observation sequence
 * @param <R> the type of result
 */
public interface Viterbi<I,R extends Result> extends Function<I,R> {

    /**
     * Builder for constructing an instance of <code>Viterbi</code>
     *
     * @param <I> the type of an observation sequence
     * @param <O> the type of an output sequence
     * @param <R> the type of result
     */
    class Builder<I, O, R extends Result> {

        private HMM hmm;
        private int T;
        private boolean threadSafe;
        private ObservationEncoder<I> observationEncoder;
        private ObservationDecoder<I> observationDecoder;
        private PathDecoder<O> pathDecoder;
        private String resultFactoryClass;

        private boolean timeLogging;

        public Viterbi<I, R> build() {
            try {
                @SuppressWarnings("unchecked") // Will throw exception on
                        Class<ResultFactory<I, O, R>> clazz = (Class<ResultFactory<I, O, R>>) Class.forName(resultFactoryClass);
                Constructor<ResultFactory<I, O, R>> cons = clazz.getConstructor(ObservationDecoder.class, PathDecoder.class);
                ResultFactory<I, O, R> resultFactory = cons.newInstance(observationDecoder, pathDecoder);
                Viterbi<I, R> viterbi = new ViterbiImpl<>(hmm, T, observationEncoder, resultFactory);
                viterbi = timeLogging ? new TimeLoggingViterbi(viterbi) : viterbi;
                viterbi = threadSafe ? new ThreadSafeViterbi<>(viterbi) : viterbi;
                return viterbi;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        public Builder<I, O, R> withHMM(HMM hmm) {
            this.hmm = hmm;
            return this;
        }

        /**
         * The maximum length of any input sequence. The instance of
         * {@code viterbi} will use memory proportional to T squared.
         */
        public Builder<I, O, R> withMaxObservationLength(int T) {
            this.T = T;
            return this;
        }

        public Builder<I, O, R> withThreadSafety() {
            threadSafe = true;
            return this;
        }

        /**
         * Specify which {@code PathEncoder} to use.
         */
        public Builder<I, O, R> withObservationEncoder(ObservationEncoder<I> observationEncoder) {
            this.observationEncoder = observationEncoder;
            return this;
        }

        /**
         * Specify which {@code ObservationDecoder} to use.
         */
        public Builder<I, O, R> withObservationDecoder(ObservationDecoder<I> observationDecoder) {
            this.observationDecoder = observationDecoder;
            return this;
        }

        /**
         * Specify which {@code PathDecoder} to use.
         */
        public Builder<I, O, R> withPathDecoder(PathDecoder<O> pathDecoder) {
            this.pathDecoder = pathDecoder;
            return this;
        }

        /**
         * Specify the fully qualified name of the {@link ResultFactory} to use.
         * The {@code ResultFactory} will be instantiated with Reflection.
         */
        public Builder<I, O, R> withResultFactoryClass(String resultFactoryClass) {
            this.resultFactoryClass = resultFactoryClass;
            return this;
        }

        public Builder<I, O, R> withTimeLogging() {
            this.timeLogging = true;
            return this;
        }
    }
}