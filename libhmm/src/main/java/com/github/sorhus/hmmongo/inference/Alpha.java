package com.github.sorhus.hmmongo.inference;

import com.github.sorhus.hmmongo.hmm.HMM;

public class Alpha {

    private final HMM hmm;
    private final int T;

    public Alpha(HMM hmm, int T) {
        this.hmm = hmm;
        this.T = T;
    }

    public double[] alpha(int t, int[] observations) {
        final double[][] alpha = new double[T][];
        for (int u = 0; u < t + 1; u++) {
            alpha(u, observations, alpha);
        }
        return alpha[t];
    }

    private void alpha(int t, int[] observations, double[][] alpha) {
        alpha[t] = new double[hmm.n];
        int j = observations[t];
        for(int i = 0; i < hmm.n; i++) {
            double p = hmm.B[i][j];
//            double p = Math.exp(hmm.B[i][j]);
            double a = 0.0;
            if(t == 0) {
                a = hmm.pi[i];
//                a = Math.exp(hmm.pi[i]);
            } else {
                for (int k = 0; k < hmm.n; k++) {
                    a += Math.exp(hmm.A.get(i).get(k) + alpha[t-1][k]);
//                    a += Math.exp(hmm.A.get(i).get(k)) * alpha[t-1][k];
                }
                a = Math.log(a);
            }
            alpha[t][i] = p + a;
//            alpha[t][i] = p * a;
        }
    }


}
