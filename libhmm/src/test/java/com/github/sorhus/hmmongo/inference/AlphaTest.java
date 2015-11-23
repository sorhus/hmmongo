package com.github.sorhus.hmmongo.inference;

import com.github.sorhus.hmmongo.hmm.HMM;
import org.junit.Test;
import java.io.IOException;

import static org.junit.Assert.assertArrayEquals;

public class AlphaTest {

    @Test
    public void testAlpha() throws IOException {

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
        Alpha instance = new Alpha(hmm, 5);

        double[][] expected = {
            {0.8182, 0.1818},
            {0.8834, 0.1166},
            {0.1907, 0.8093},
            {0.7308, 0.2692},
            {0.8673, 0.1327}
        };

        if(log) {
            for (double[] e : expected) {
                for (int i = 0; i < e.length; i++) {
                    e[i] = Math.log(e[i]);
                }
            }
        }

        for (int t = 0; t < expected.length; t++) {
            double[] result = instance.alpha(t, observations);
            Normaliser.normalise(result, log);
            assertArrayEquals(expected[t], result, 1e-3);
        }
    }

}