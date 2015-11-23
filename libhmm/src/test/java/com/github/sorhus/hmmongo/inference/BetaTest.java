package com.github.sorhus.hmmongo.inference;

import com.github.sorhus.hmmongo.hmm.HMM;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;

public class BetaTest {

    @Test
    public void testBeta() throws Exception {

        final boolean log = true;

        double[] pi = {0.5, 0.5};

        double[][] A = {
            {0.7, 0.3},
            {0.3, 0.7}
        };

        double[][] B = {
            {0.9, 0.1},
            {0.2, 0.8}
        };
        HMM hmm = new HMM.Builder().fromArrays(pi, A, B).build();

        int[] observations = {0, 0, 1, 0, 0};
        Beta instance = new Beta(hmm, 5);

        double[][] expected = {
            {0.6469, 0.3531},
            {0.5923, 0.4077},
            {0.3763, 0.6237},
            {0.6533, 0.3467},
            {0.6273, 0.3727},
        };

        if(log) {
            for (double[] e : expected) {
                for (int i = 0; i < e.length; i++) {
                    e[i] = Math.log(e[i]);
                }
            }
        }

        for (int t = 0; t < observations.length; t++) {
            double[] result = instance.beta(t, observations);
            Normaliser.normalise(result, log);
            assertArrayEquals(expected[t], result, 1e-3);
        }
        double[] expected_ = log ? new double[] {0.0, 0.0} : new double[] {1.0, 1.0};
        assertArrayEquals(expected_, instance.beta(5, observations), 1e-3);
    }
}
