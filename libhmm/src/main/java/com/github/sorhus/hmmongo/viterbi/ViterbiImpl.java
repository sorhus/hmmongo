package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.viterbi.result.Result;
import com.github.sorhus.hmmongo.viterbi.result.ResultFactory;

class ViterbiImpl<I,O,R extends Result> implements Viterbi<I,R> {

    final HMM hmm;
    final double[][] PHI;
    final int[][] PSI;
    final ObservationEncoder<I> encoder;
    final ResultFactory<I,O,R> resultFactory;

    ViterbiImpl(HMM hmm, int T, ObservationEncoder<I> encoder, ResultFactory<I,O,R> resultFactory) {
        this.hmm = hmm;
        this.PHI = new double[T][];
        this.PSI = new int[T][];
        for(int i = 0; i < T; i++) {
            PHI[i] = new double[hmm.n];
            PSI[i] = new int[hmm.n];
        }
        this.encoder = encoder;
        this.resultFactory = resultFactory;
    }

    @Override
    public R apply(I input) {
        int[] observations = encoder.apply(input);
        if(observations.length > 0) {
            initialise(observations[0]);
            recurse(observations);
            return terminate(observations);
        } else {
            return resultFactory.noPath();
        }
    }

    private void initialise(int first) {
        for(int i = 0; i < hmm.n; i++) {
            PHI[0][i] = hmm.pi[i] + hmm.B[i][first];
            PSI[0][i] = -1;
        }
    }

    private void recurse(int[] observations) {
        for(int t = 1; t < observations.length; t++) {
            for(int j = 0; j < hmm.n; j++) {
                final double b = hmm.B[j][observations[t]];
                double max = Double.NEGATIVE_INFINITY;
                int argmax = -1;
                for (int i : hmm.A.get(j).keys()) {
                    final double v = PHI[t - 1][i] + hmm.A.get(j).get(i);
                    if (v > max) {
                        max = v;
                        argmax = i;
                    }
                }
                PHI[t][j] = max + b;
                PSI[t][j] = PHI[t][j] == Double.NEGATIVE_INFINITY ? -1 : argmax;
            }
        }
    }

    private R terminate(int[] observations) {
        int T = observations.length;
        int path[] = new int[T];
        double max = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < hmm.n; i++) {
            if(PHI[T - 1][i] > max) {
                max = PHI[T - 1][i];
                path[T - 1] = i;
            }
        }
        for(int t = T - 2; t > -1; t--) {
            if(path[t + 1] < 0) {
                return resultFactory.noPath();
            }
            path[t] = PSI[t + 1][path[t + 1]];
        }
        return resultFactory.apply(observations, path, max);
    }

}
