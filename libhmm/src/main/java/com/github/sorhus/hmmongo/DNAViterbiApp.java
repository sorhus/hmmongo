package com.github.sorhus.hmmongo;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.hmm.HMMBuilder;
import com.github.sorhus.hmmongo.util.FailFastBufferedWriter;
import com.github.sorhus.hmmongo.viterbi.*;
import com.github.sorhus.hmmongo.viterbi.result.FullResult;
import com.github.sorhus.hmmongo.viterbi.result.Result;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.InputStream;
import java.util.function.Function;

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

        Function<String, InputStream> r = DNAViterbiApp.class::getResourceAsStream;
        HMM hmm = new HMMBuilder()
            .fromInputStreams(r.apply(args[0]), r.apply(args[1]), r.apply(args[2]))
            .adjacency()
            .build();
        Viterbi<String,FullResult> viterbi =
            new ViterbiBuilder<String,String,FullResult>()
            .withHMM(hmm)
            .withMaxObservationLength(Integer.parseInt(args[3]))
            .withObservationEncoder(new DNAEncoder())
            .withObservationDecoder(new DNADecoder())
            .withPathDecoder(new StringDecoder())
            .withResultFactoryClass("com.github.sorhus.hmmongo.viterbi.result.FullResultFactory")
            .build();

        try(FailFastBufferedWriter writer = new FailFastBufferedWriter(args[5])) {
            new BufferedReader(new FileReader(args[4])).lines()
                .map(viterbi)
                .map(Result::toString)
                .forEachOrdered(writer::write);
        }

    }
}
