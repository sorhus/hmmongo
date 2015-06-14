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

        /**
         * Finalise this builder and retrieve an instance of {@code Viterbi} as specified.
         * @return the {@code Viterbi} instance
         * @throws RuntimeException if the specification is not complete
         */
        public Viterbi<I, R> build() {
            if(hmm == null || T <= 0 || observationEncoder == null || pathDecoder == null || resultFactoryClass == null) {
                throw new RuntimeException("Incomplete specification");
            }
            if(resultFactoryClass.endsWith("FullResultFactory") && observationDecoder == null) {
                throw new RuntimeException("Incomplete specification");
            }
            try {
                @SuppressWarnings("unchecked")
                Class<ResultFactory<I, O, R>> clazz = (Class<ResultFactory<I, O, R>>) Class.forName(resultFactoryClass);
                Constructor<ResultFactory<I, O, R>> cons = clazz.getConstructor(ObservationDecoder.class, PathDecoder.class);
                ResultFactory<I, O, R> resultFactory = cons.newInstance(observationDecoder, pathDecoder);
                Viterbi<I, R> viterbi = new ViterbiImpl<>(hmm, T, observationEncoder, resultFactory);
                viterbi = timeLogging ? new TimeLoggingViterbi<>(viterbi) : viterbi;
                viterbi = threadSafe ? new ThreadSafeViterbi<>(viterbi) : viterbi;
                return viterbi;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        /**
         * Specify the {@code HMM} to use.
         */
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

        /**
         * Makes it possible to execute viterbi from multiple threads without
         * getting getting inconsistent results. Note that there is no
         * functionality for concurrency here.
         */
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
         * Specify which {@code ObservationDecoder} to use. It will be past on to the
         * {@link ResultFactory}
         */
        public Builder<I, O, R> withObservationDecoder(ObservationDecoder<I> observationDecoder) {
            this.observationDecoder = observationDecoder;
            return this;
        }

        /**
         * Specify which {@code PathDecoder} to use. It will be past on to the
         * {@link ResultFactory}
         */
        public Builder<I, O, R> withPathDecoder(PathDecoder<O> pathDecoder) {
            this.pathDecoder = pathDecoder;
            return this;
        }

        /**
         * Specify the fully qualified name of the {@link ResultFactory} to use.
         * The {@code ResultFactory} will be instantiated with reflection.
         */
        public Builder<I, O, R> withResultFactoryClass(String resultFactoryClass) {
            this.resultFactoryClass = resultFactoryClass;
            return this;
        }

        /**
         * Apply some naive time logging to stdout, possibly useful for testing.
         */
        public Builder<I, O, R> withTimeLogging() {
            this.timeLogging = true;
            return this;
        }
    }
}