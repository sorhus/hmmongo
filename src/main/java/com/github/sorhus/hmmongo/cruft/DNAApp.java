package com.github.sorhus.hmmongo.cruft;

import com.github.sorhus.hmmongo.DNAEncoder;
import com.github.sorhus.hmmongo.HMM;
import com.github.sorhus.hmmongo.HMMFactory;
import com.github.sorhus.hmmongo.Viterbi;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class DNAApp {

    public static void main(String[] args) throws IOException {
        HMM hmm = HMMFactory.fromFiles(args[0], args[1], args[2]);
        System.out.println("Read in HMM");
        int T = Integer.parseInt(args[3]);
        Viterbi viterbi = new Viterbi(hmm.toLog(), T);
        Scanner sc = new Scanner(new BufferedReader(new FileReader(args[4])));
        System.out.println("Found observations");
        while(sc.hasNext()) {
            char[] chars = sc.nextLine().toLowerCase().toCharArray();
            int[] observations = new int[chars.length];
            for (int i = 0; i < chars.length; i++) {
                observations[i] = DNAEncoder.encode(chars[i]);
            }
            int[] path = viterbi.getPath(observations);
            if(path == null) {
                System.out.println("No path possible");
            } else {
                System.out.println("Found optimal path");
            }
        }
        System.out.println("Processed all observed sequences");
    }

}
