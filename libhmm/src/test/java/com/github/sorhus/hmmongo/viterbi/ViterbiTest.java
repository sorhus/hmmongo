package com.github.sorhus.hmmongo.viterbi;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.hmm.HMMBuilder;
import com.github.sorhus.hmmongo.viterbi.result.BasicResult;
import com.github.sorhus.hmmongo.viterbi.result.BasicResultFactory;
import com.github.sorhus.hmmongo.viterbi.result.ResultFactory;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class ViterbiTest {

    static double[] pi = {1.0/3, 1.0/3, 1.0/3};
    static double[][] A = {{0, 2.0/5, 3.0/5}, {1.0/5, 0, 4.0/5}, {0, 0, 1.0}};
    static double[][] B = {{0, 1.0}, {9.0/10, 1.0/10}, {1.0/2, 1.0/2}};

    static int[] input = {0, 1, 0};
    static int[] expected = {1, 2, 2};
    static double[][] expectedPHI = {{0.0, 0.3, 1.0/6}, {3.0/50, 0.0, 6.0/50}, {0.0, 27.0/1250, 75.0/1250}};
    static int[][] expectedPSI = {{-1, -1, -1}, {1, -1, 1}, {-1, 0, 2}};

    static double[][] expectedLogPHI = {{0, 0, 0}, {0, 0, 0}, {0, 0, 0}};
    static {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                expectedLogPHI[i][j] = Math.log(expectedPHI[i][j]);
            }
        }
    }

    @Test
    public void testAdjacencyLog() throws IOException {
        HMM hmm = new HMMBuilder().fromArrays(pi, A, B).adjacency().build();
        ResultFactory<int[],int[],BasicResult<int[]>> resultFactory =
                new BasicResultFactory<>((i) -> i, (i) -> i);
        ViterbiImpl<int[],int[],BasicResult<int[]>> instance =
                new ViterbiImpl<>(hmm, 3, (i) -> i, resultFactory);
        check(instance);
    }

    @Test
    public void testMatrixLog() throws IOException {
        HMM hmm = new HMMBuilder().fromArrays(pi, A, B).build();
        ResultFactory<int[],int[],BasicResult<int[]>> resultFactory =
                new BasicResultFactory<>((i) -> i, (i) -> i);
        ViterbiImpl<int[],int[],BasicResult<int[]>> instance =
                new ViterbiImpl<>(hmm, 3, (i) -> i, resultFactory);
        check(instance);
    }

    public void check(ViterbiImpl<int[],int[],BasicResult<int[]>> instance) {
        BasicResult<int[]> result = instance.apply(input);
        assertArrayEquals(expected, result.path);
        for (int i = 0; i < 3; i++) {
            assertArrayEquals(expectedLogPHI[i], instance.PHI[i], 1e-10);
            assertArrayEquals(expectedPSI[i], instance.PSI[i]);
        }
    }
}
