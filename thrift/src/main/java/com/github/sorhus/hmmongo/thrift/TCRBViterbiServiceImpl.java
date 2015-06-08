package com.github.sorhus.hmmongo.thrift;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.hmm.HMMBuilder;
import com.github.sorhus.hmmongo.thrift.TCRBViterbiService;
import com.github.sorhus.hmmongo.viterbi.*;
import com.github.sorhus.hmmongo.viterbi.result.FullResult;
import org.apache.thrift.TException;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Function;

public class TCRBViterbiServiceImpl implements TCRBViterbiService.Iface {

    private final Viterbi<String, FullResult<String, String>> viterbi;

    public TCRBViterbiServiceImpl() throws IOException {
        Function<String, InputStream> r = TCRBViterbiServiceImpl.class::getResourceAsStream;
        HMM hmm = new HMMBuilder()
            .fromInputStreams(r.apply("/example_pi.gz"), r.apply("/example_A.gz"), r.apply("/example_B.gz"))
            .adjacency()
            .build();

        viterbi = new ViterbiBuilder<String, String, FullResult<String, String>>()
            .withHMM(hmm)
            .withMaxObservationLength(101)
            .withObservationEncoder(new DNAEncoder())
            .withObservationDecoder(new DNADecoder())
            .withPathDecoder(new StringDecoder())
            .withResultFactoryClass("com.github.sorhus.hmmongo.viterbi.result.FullResultFactory")
            .withThreadSafety()
            .withTimeLogging()
            .build();

    }

    @Override
    public com.github.sorhus.hmmongo.thrift.FullResult apply(String input) throws TException {
        FullResult<String, String> result = viterbi.apply(input);
        return new com.github.sorhus.hmmongo.thrift.FullResult(result.input, result.path, result.likelihood);
    }
}
