package com.github.sorhus.hmmongo;

import java.io.Serializable;

/**
 * Hidden Markov Model. Defined by the initial distribution,
 * the state transition distributions and the emission distributions.
 *
 * States and emissions are both encoded as consecutive nonnegative integers
 * Distributions are either probabilities or log probabilities
 */
public class HMM implements Serializable {

    /**
     * Initial distribution
     */
    final public double[] pi;

    /**
     * State transition distributions
     */
    final public double[][] A;

    /**
     * Emission distributions
     */
    final public double[][] B;

    /**
     * Size of the state space
     */
    final public int n;

    /**
     * Whether or not this HMM is represented in logarithms
     */
    final public boolean log;

    public HMM(double[] pi, double[][] A, double[][] B, boolean log) {
        this.pi = pi;
        this.A = A;
        this.B = B;
        this.log = log;
        this.n = pi.length;
        verify();
    }
    public HMM(double[] pi, double[][] A, double[][] B) {
        this(pi, A, B, false);
    }

    private void verify() {
        if (A.length != n || B.length != n) {
            throw new IllegalArgumentException();
        }
        for (double[] a : A) {
            if (a.length != n) {
                throw new IllegalArgumentException();
            }
        }
        int m = B[0].length;
        for (double[] b : B) {
            if (b.length != m) {
                throw new IllegalArgumentException();
            }
        }
    }

    public HMM toLog() {
        double[] pi_n = new double[n];
        double[][] A_n = new double[n][];
        double[][] B_n = new double[n][];
        for (int i = 0; i < n; i++) {
            pi_n[i] = Math.log(pi[i]);
            A_n[i] = new double[n];
            for (int j = 0; j < n; j++) {
                A_n[i][j] = Math.log(A[i][j]);
            }
            B_n[i] = new double[B[i].length];
            for (int j = 0; j < B[i].length; j++) {
                B_n[i][j] = Math.log(B[i][j]);
            }
        }
        return new HMM(pi_n, A_n, B_n, true);
    }

    public HMM fromLog() {
        double[] pi_n = new double[n];
        double[][] A_n = new double[n][];
        double[][] B_n = new double[n][];
        for (int i = 0; i < n; i++) {
            pi_n[i] = Math.exp(pi[i]);
            A_n[i] = new double[n];
            for (int j = 0; j < n; j++) {
                A_n[i][j] = Math.exp(A[i][j]);
            }
            B_n[i] = new double[B[i].length];
            for (int j = 0; j < B[i].length; j++) {
                B_n[i][j] = Math.exp(B[i][j]);
            }
        }
        return new HMM(pi_n, A_n, B_n, false);
    }

}
