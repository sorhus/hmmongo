package com.github.sorhus.hmmongo.cruft;

import com.github.sorhus.hmmongo.HMM;
import com.github.sorhus.hmmongo.Viterbi;
import com.github.sorhus.hmmongo.cruft.ExampleHMM;

import java.util.Random;

public class ProfiledExampleHMM extends ExampleHMM {

    public static void main(String[] args) {
        int T = 1000000;
        int n = 1000;
        int[] observations = new int[T];
        HMM hmm = new ExampleHMM().toLog();
        Viterbi viterbi = new Viterbi(hmm, T);
        Random rnd = new Random();

        System.out.println("Running viterbi on " + n + " random sequences");
        for (int i = 0; i < n; i++) {
            for (int t = 0; t < T; t++) {
                observations[t] = rnd.nextBoolean() ? 1 : 0;
            }
            int[] result = viterbi.getPath(observations);
        }
        System.out.println("Done");

    }
}
