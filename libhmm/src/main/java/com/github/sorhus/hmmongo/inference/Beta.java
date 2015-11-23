package com.github.sorhus.hmmongo.inference;

import com.github.sorhus.hmmongo.hmm.HMM;

public class Beta {

    private final HMM hmm;
    public final int T;

    public Beta(HMM hmm, int T) {
        this.hmm = hmm;
        this.T = T;
    }

    public double[] beta(int t, int[] observations) {
        double[][] beta = new double[T + 1][];
        for (int u = beta.length - 1; u > t - 1; u--) {
            beta(u, observations, beta);
        }
        return beta[t];
    }

    private void beta(int t, int[] observations, double[][] beta) {
        beta[t] = new double[hmm.n];
        for (int i = 0; i < hmm.n; i++) {
            double b = 0.0;
            if(t == beta.length - 1) {
                b = 1;
            } else {
                int j = observations[t];
                for (int k = 0; k < hmm.n; k++) {
                    b += Math.exp(hmm.B[k][j] + hmm.A.get(i).get(k) + beta[t+1][k]);
//                    b += Math.exp(hmm.B[k][j]) * Math.exp(hmm.A.get(i).get(k)) * beta[t+1][k];
                }
            }
            beta[t][i] = Math.log(b);
//            beta[t][i] = b;
        }
    }
}
