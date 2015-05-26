package com.github.sorhus.hmmongo;

import java.util.function.DoubleBinaryOperator;

public class ExperimentalViterbiImpl implements Viterbi<int[], int[]> {

    final HMM hmm;
    final double[][] PHI;
    final int[][] PSI;

    final double min;
    final DoubleBinaryOperator op;

    public ExperimentalViterbiImpl(final HMM hmm, final int T) {
        this.hmm = hmm;
        this.PHI = new double[T][];
        this.PSI = new int[T][];
        for(int i = 0; i < T; i++) {
            PHI[i] = new double[hmm.n];
            PSI[i] = new int[hmm.n];
        }
        this.min = hmm.log ? Double.NEGATIVE_INFINITY : 0.0;
        this.op = (d1, d2) -> hmm.log ? d1 + d2 : d1 * d2;
    }

    @Override
    public Result<int[]> apply(int[] observations) {
        if(observations.length == 0) {
            return Result.NO_PATH;
        }
        initialise(observations[0]);
        recurse(observations);
        return terminate(observations.length);
    }

    protected void initialise(int first) {
        for(int i = 0; i < hmm.n; i++) {
            PHI[0][i] = op.applyAsDouble(hmm.pi[i], hmm.B[i][first]);
        }
    }

    protected void recurse(int[] observations) {
        for(int t = 1; t < observations.length; t++) {
            for(int j = 0; j < hmm.n; j++) {
                double max = min;
                int argmax = -1;
                for (int i : hmm.A.get(j).keys()) {
                    final double v = op.applyAsDouble(PHI[t - 1][i], hmm.A.get(j).get(i));
                    if (v > max) {
                        max = v;
                        argmax = i;
                    }
                }
                PHI[t][j] = op.applyAsDouble(max, hmm.B[j][observations[t]]);
                PSI[t][j] = PHI[t][j] == min ? -1 : argmax;
            }
        }
    }

    protected Result<int[]> terminate(int observations) {
        int path[] = new int[observations];
        double max = min;
        for(int i = 0; i < hmm.n; i++) {
            if(PHI[observations - 1][i] > max) {
                max = PHI[observations - 1][i];
                path[observations - 1] = i;
            }
        }
        for(int t = observations - 2; t > -1; t--) {
            if(path[t + 1] < 0) {
                return Result.NO_PATH;
            }
            path[t] = PSI[t + 1][path[t + 1]];
        }
        return new Result<>(path, max);
    }

}
