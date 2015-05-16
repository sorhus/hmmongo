package com.github.sorhus.hmmongo;

import java.io.Serializable;

/**
 * Implementation of the Viterbi algorithm
 * Can handle probabilities as well as log probabilities
 * Will be slow for sparse models
 */
public class Viterbi implements Serializable {

    final HMM hmm;

    final double[][] PHI;
    final int[][] PSI;

    /**
     *
     * @param hmm
     * @param T maximum sequence length
     */
    public Viterbi(HMM hmm, int T) {
        this.hmm = hmm;
        this.PHI = new double[T][];
        this.PSI = new int[T][];
        for(int i = 0; i < T; i++) {
            PHI[i] = new double[hmm.n];
            PSI[i] = new int[hmm.n];
        }
    }

    public int[] getPath(int[] observations) {
        initialise(observations[0]);
        recurse(observations);
        return terminate(observations.length);
    }

    private void initialise(int first) {
        for(int i = 0; i < hmm.n; i++) {
            PHI[0][i] = hmm.log ?
                    hmm.pi[i] + hmm.B[i][first] :
                    hmm.pi[i] * hmm.B[i][first];
            PSI[0][i] = -1;
        }
    }

    private void recurse(int[] observations) {
        for(int t = 1; t < observations.length; t++) {
            for(int j = 0; j < hmm.n; j++) {
                double max = Double.NEGATIVE_INFINITY;
                int argmax = -1;
                double b = 0.0;
                for(int i = 0; i < hmm.n; i++) {
                    final double v = hmm.log ?
                            PHI[t - 1][i] + hmm.A[i][j] :
                            PHI[t - 1][i] * hmm.A[i][j];
                    if(v > max) {
                        max = v;
                        argmax = i;
                        b = hmm.B[j][observations[t]];
                    }
                }
                PHI[t][j] = hmm.log ?
                        max + b :
                        max * b;

                PSI[t][j] = PHI[t][j] == (hmm.log ? Double.NEGATIVE_INFINITY : 0.0) ?
                        -1 :
                        argmax;
            }
        }
    }

    private int[] terminate(int observations) {
        int path[] = new int[observations];
        double max = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < hmm.n; i++) {
            if(PHI[observations - 1][i] > max) {
                max = PHI[observations - 1][i];
                path[observations - 1] = i;
            }
        }
        for(int t = observations - 2; t > -1; t--) {
            if(path[t+1] < 0) {
                return null;
            }
            path[t] = PSI[t+1][path[t+1]];
        }
        return path;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("PHI:\n");
        for(double[] phi : PHI) {
            for(double p : phi) {
                sb.append(p).append(" ");
            }
            sb.append("\n");
        }
        sb.append("\n");
        sb.append("PSI:\n");
        for(int[] psi : PSI) {
            for(int p : psi) {
                sb.append(p).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
