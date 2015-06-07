package com.github.sorhus.hmmongo;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.hmm.HMMBuilder;
import com.github.sorhus.hmmongo.util.FailFastBufferedWriter;
import com.github.sorhus.hmmongo.viterbi.*;
import com.github.sorhus.hmmongo.viterbi.result.BasicResult;
import com.github.sorhus.hmmongo.viterbi.result.Result;

import java.io.BufferedReader;
import java.io.FileReader;

public class DNAViterbiApp {

    /**
     * Run viterbi on `input` using specified the HMM
     * Write each result to `output`
     * The HMM is specified by `pi`, `A` and `B`,
     * `T` is max length of any input sequence.
     *
     * @param args = {pi, A, B, T, input, output};
     * @throws Exception if something goes wrong
     */
    public static void main(String[] args) throws Exception {

        HMM hmm = new HMMBuilder()
                .fromFiles(args[0], args[1], args[2])
                .adjacency()
                .build();
        Viterbi<String,String> viterbi = new ViterbiBuilder<String,String>()
            .withHMM(hmm)
            .withMaxObservationLength(Integer.parseInt(args[3]))
            .withObservationEncoder(new DNAEncoder())
            .withObservationDecoder(new DNADecoder())
            .withPathDecoder(new StringDecoder())
            .withResultFactoryClass("com.github.sorhus.hmmongo.result.FullResultFactory")
            .build();

        try(FailFastBufferedWriter writer = new FailFastBufferedWriter(args[5])) {
            new BufferedReader(new FileReader(args[4])).lines()
                .map(viterbi)
                .map(Result::toString)
                .forEachOrdered(writer::write);
        }

    }
}
