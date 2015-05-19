package com.github.sorhus.hmmongo;

import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

// TODO refactor to use HMMBuilder
public class ViterbiTest<V extends Viterbi> {

    static double[] pi = {1.0 / 3, 1.0 / 3, 1.0 / 3};
    static double[][] A = {{0, 2.0 / 5, 3.0 / 5}, {1.0 / 5, 0, 4.0 / 5}, {0, 0, 1.0}};
    static double[][] B = {{0, 1.0}, {9.0 / 10, 1.0 / 10}, {1.0 / 2, 1.0 / 2}};

    static int[] input = {0, 1, 0};
    static int[] expected = {1, 2, 2};
    static double[][] expectedPHI = {{0.0, 0.3, 1.0 / 6}, {3.0 / 50, 0.0, 6.0 / 50}, {0.0, 27.0 / 1250, 75.0 / 1250}};
    static int[][] expectedPSI = {{-1, -1, -1}, {1, -1, 1}, {-1, 0, 2}};

    static double[][] expectedLogPHI = {{0,0,0},{0,0,0},{0,0,0}};
    static {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                expectedLogPHI[i][j] = Math.log(expectedPHI[i][j]);
            }
        }
    }

    @Test
    public void testAdjacencyRegular() {
        HMM hmm = new HMM(pi, HMMFactory.getKeyedListFromMatrix(A), B, false);
        Viterbi instance = new Viterbi(HMMFactory.fromMatrixToAdjacency(hmm), 3);
        testRegular(instance);
    }

    @Test
    public void testAdjacencyLog() {
        HMM hmm = new HMM(pi, HMMFactory.getKeyedListFromMatrix(A), B, false);
        Viterbi instance = new Viterbi(HMMFactory.fromMatrixToAdjacency(HMMFactory.toLog(hmm)), 3);
        testLog(instance);
    }

    @Test
    public void testMatrixRegular() {
        HMM hmm = new HMM(pi, HMMFactory.getKeyedListFromMatrix(A), B, false);
        Viterbi instance = new Viterbi(HMMFactory.fromMatrixToAdjacency(hmm), 3);
        testRegular(instance);
    }

    @Test
    public void testMatrixLog() {
        HMM hmm = new HMM(pi, HMMFactory.getKeyedListFromMatrix(A), B, false);
        Viterbi instance = new Viterbi(HMMFactory.fromMatrixToAdjacency(HMMFactory.toLog(hmm)), 3);
        testLog(instance);
    }


    public void testRegular(Viterbi instance) {
        ViterbiResult result = instance.getPath(input);
        assertArrayEquals(expected, result.path);
        for (int i = 0; i < 3; i++) {
            assertArrayEquals(expectedPHI[i], instance.PHI[i], 1e-10);
            assertArrayEquals(expectedPSI[i], instance.PSI[i]);
        }
    }

    public void testLog(Viterbi instance) {
        ViterbiResult result = instance.getPath(input);
        assertArrayEquals(expected, result.path);
        for (int i = 0; i < 3; i++) {
            assertArrayEquals(expectedLogPHI[i], instance.PHI[i], 1e-10);
            assertArrayEquals(expectedPSI[i], instance.PSI[i]);
        }
    }
}
