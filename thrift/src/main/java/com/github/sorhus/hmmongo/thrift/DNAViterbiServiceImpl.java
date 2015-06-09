package com.github.sorhus.hmmongo.thrift;

import com.github.sorhus.hmmongo.viterbi.Viterbi;
import org.apache.thrift.TException;

import java.io.IOException;

public class DNAViterbiServiceImpl implements DNAViterbiService.Iface {

    private final Viterbi<String, FullResult> viterbi;

    public DNAViterbiServiceImpl(Viterbi<String, FullResult> viterbi) throws IOException {
        this.viterbi = viterbi;
    }

    @Override
    public FullResult apply(String input) throws TException {
        return viterbi.apply(input);
    }
}
