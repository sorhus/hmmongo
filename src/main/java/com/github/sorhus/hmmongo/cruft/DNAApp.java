package com.github.sorhus.hmmongo.cruft;

import com.github.sorhus.hmmongo.*;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DNAApp {

    public static void main(String[] args) throws IOException {
        HMM mHmm = HMMFactory.toLog(HMMFactory.fromFiles(args[0], args[1], args[2]));
        HMM aHmm = HMMFactory.fromMatrixToAdjacency(mHmm);
        System.out.println("Read in HMM");
        int T = Integer.parseInt(args[3]);
        Viterbi mViterbi = new Viterbi(mHmm, T);
        Viterbi aViterbi = new Viterbi(aHmm, T);
        Scanner sc = new Scanner(new BufferedReader(new FileReader(args[4])));
        System.out.println("Found observations");
        while(sc.hasNext()) {
            char[] chars = sc.nextLine().toLowerCase().toCharArray();
            int[] observations = new int[chars.length];
            for (int i = 0; i < chars.length; i++) {
                observations[i] = DNAEncoder.encode(chars[i]);
            }
            long t = System.currentTimeMillis();
            ViterbiResult mResult = mViterbi.getPath(observations);
            print(mResult, t);
            t = System.currentTimeMillis();
            ViterbiResult aResult = aViterbi.getPath(observations);
            print(aResult, t);
        }
        System.out.println("Processed all observed sequences");

    }

    private static void print(ViterbiResult result, long t) {
        if(result.likelihood == Double.NEGATIVE_INFINITY) {
            System.out.println("No path possible");
        } else {
            System.out.println("Found optimal path: " + result.likelihood);
            for(int p : result.path) {
                System.out.print(p + " ");
            }
            System.out.println();
        }
        System.out.println("Took: " + (System.currentTimeMillis() - t) + " ms");
    }
}
