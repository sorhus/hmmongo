package com.github.sorhus.hmmongo.cruft;

import com.github.sorhus.hmmongo.HMM;
import com.github.sorhus.hmmongo.Viterbi;

public class ExampleHMM extends HMM {

    static double[] pi = {1.0 / 3, 1.0 / 3, 1.0 / 3};
    static double[][] A = {{0, 2.0 / 5, 3.0 / 5}, {1.0 / 5, 0, 4.0 / 5}, {0, 0, 1.0}};
    static double[][] B = {{0, 1.0}, {9.0 / 10, 1.0 / 10}, {1.0 / 2, 1.0 / 2}};

    public ExampleHMM() {
        super(ExampleHMM.pi, ExampleHMM.A, ExampleHMM.B);
    }

    public static void main(String[] args) {
        HMM hmm = new ExampleHMM();
        int[] observations = {0, 1, 0};

        Viterbi viterbi = new Viterbi(hmm, 3);
        int[] result = viterbi.getPath(observations);
        print(viterbi, result);

        Viterbi logViterbi = new Viterbi(hmm.toLog(), 3);
        int[] logResult = logViterbi.getPath(observations);
        print(logViterbi, logResult);

    }

    public static void print(Viterbi viterbi, int[] result) {
        System.out.println();
        System.out.print("Optimal path is: ");
        for (int t = 0; t < result.length; t++) {
            System.out.print(result[t] + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println(viterbi.toString());
    }
}
