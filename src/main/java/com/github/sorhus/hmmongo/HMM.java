package com.github.sorhus.hmmongo;

/**
 * Hidden Markov Model. Defined by the initial distribution,
 * the state transition distributions and the emission distributions.
 *
 * States and emissions are both encoded as consecutive nonnegative integers
 */
public class HMM {

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

    public HMM(double[] pi, double[][] A, double[][] B) {
        this.pi = pi;
        this.A = A;
        this.B = B;
        this.n = pi.length;
        verify();
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
}
