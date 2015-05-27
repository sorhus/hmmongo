package com.github.sorhus.hmmongo;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.hmm.HMMBuilder;
import com.github.sorhus.hmmongo.util.FailFastBufferedWriter;
import com.github.sorhus.hmmongo.viterbi.Result;
import com.github.sorhus.hmmongo.viterbi.Viterbi;
import com.github.sorhus.hmmongo.viterbi.ViterbiBuilder;

import java.io.*;

public class DNAViterbiApp {

    /**
     * Run viterbi on `input` using specified the HMM
     * Write each result to `output`
     * The HMM is specified by `pi`, `A` and `B`,
     * and must be encoded according to `DNAEncoder`
     * `T` is max length of any input sequence
     *
     * @param args = {pi, A, B, T, input, output};
     * @throws Exception if something goes wrong
     */
    public static void main(String[] args) throws Exception {

        HMM hmm = new HMMBuilder().fromFiles(args[0], args[1], args[2]).adjacency().asLog().build();
        Viterbi<String, String> viterbi = new ViterbiBuilder(hmm, Integer.parseInt(args[3]))
            .withEncoderDecoder(new DNAEncoder(), new StringDecoder());

        try(FailFastBufferedWriter writer = new FailFastBufferedWriter(args[5])) {
            new BufferedReader(new FileReader(args[4])).lines()
                .map(viterbi)
                .map(Result::toString)
                .forEachOrdered(writer::write);
        }

    }
}
