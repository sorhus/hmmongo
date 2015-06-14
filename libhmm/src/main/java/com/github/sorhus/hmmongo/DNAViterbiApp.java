package com.github.sorhus.hmmongo;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.viterbi.*;
import com.github.sorhus.hmmongo.viterbi.result.FullResult;
import com.github.sorhus.hmmongo.viterbi.result.Result;

import java.io.*;
import java.util.function.Function;

public class DNAViterbiApp {

    /**
     * Run viterbi on {@code input} using specified the {@code HMM}
     * Write each result to {@code output}.
     * The {@code HMM} is specified by {@code pi}, {@code A} and {@code B},
     * {@code T} is max length of any input sequence.
     *
     * It assumes the following DNA encoding
     * <br>{@code (a,0)}
     * <br>{@code (c,1)}
     * <br>{@code (g,2)}
     * <br>{@code (t,3)}
     *
     * @param args {@code {pi, A, B, T, input, output};}
     * @throws Exception if something goes wrong
     */
    public static void main(String[] args) throws Exception {

        Function<String, InputStream> r = DNAViterbiApp.class::getResourceAsStream;
        HMM hmm = new HMM.Builder()
            .fromInputStreams(r.apply(args[0]), r.apply(args[1]), r.apply(args[2]))
            .adjacency()
            .build();
        Viterbi<String,FullResult> viterbi =
            new Viterbi.Builder<String,String,FullResult>()
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

    private DNAViterbiApp() {}

    static class FailFastBufferedWriter implements AutoCloseable {

        final private BufferedWriter bw;

        public FailFastBufferedWriter(String output) {
            try {
                bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "utf-8"));
            } catch (UnsupportedEncodingException | FileNotFoundException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public void close() throws Exception {
            bw.close();
        }

        public void write(String line) {
            try {
                bw.write(line);
                bw.newLine();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
