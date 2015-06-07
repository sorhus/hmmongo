package com.github.sorhus.hmmongo.hmm;

import java.util.List;

/**
 * Hidden Markov Model. Defined by the initial distribution,
 * the state transition distributions and the emission distributions.
 *
 * States and emissions are both encoded as consecutive nonnegative integers
 * Distributions are either expressed as probabilities or log probabilities
 */
public class HMM {

    /**
     * Initial distribution
     * pi[i] is the prior to start in state i
     */
    final public double[] pi;

    /**
     * State transition distributions
     * A.get(j).get(i) is the prior to go from state i to j
     */
    final public List<Keyed> A;

    /**
     * Emission distributions
     * B[i][j] is the prior to emit j when in i
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


    public HMM(double[] pi, List<Keyed> A, double[][] B, boolean log) {
        this.pi = pi;
        this.A = A;
        this.B = B;
        this.n = pi.length;
        this.log = log;
        verify();
    }

    private void verify() {
        if (A.size() != n || B.length != n) {
            throw new IllegalArgumentException();
        }
        int m = B[0].length;
        for (double[] b : B) {
            if (b.length != m) {
                throw new IllegalArgumentException();
            }
        }
    }
}