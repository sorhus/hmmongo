package com.github.sorhus.hmmongo.thrift;

import com.github.sorhus.hmmongo.hmm.HMM;
import com.github.sorhus.hmmongo.hmm.HMMBuilder;
import com.github.sorhus.hmmongo.thrift.DNAViterbiService.Processor;
import com.github.sorhus.hmmongo.viterbi.*;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.server.TThreadPoolServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

import java.io.InputStream;
import java.util.function.Function;

public class TCRBViterbiServer {

    /**
     * args == {pi, A, B, T}
     * @param args
     */
    public static void main(String [] args) {
        try {

            Function<String, InputStream> r = DNAViterbiServiceImpl.class::getResourceAsStream;
            HMM hmm = new HMMBuilder()
                .fromInputStreams(r.apply(args[0]), r.apply(args[1]), r.apply(args[2]))
                .adjacency()
                .build();

            Viterbi<String, FullResult> viterbi = new ViterbiBuilder<String, String, FullResult>()
                .withHMM(hmm)
                .withMaxObservationLength(Integer.parseInt(args[3]))
                .withObservationEncoder(new DNAEncoder())
                .withObservationDecoder(new DNADecoder())
                .withPathDecoder(new StringDecoder())
                .withResultFactoryClass("com.github.sorhus.hmmongo.thrift.DNAResultFactory")
                .withThreadSafety()
//                .withTimeLogging()
                .build();

            DNAViterbiService.Iface handler = new DNAViterbiServiceImpl(viterbi);

            Processor processor = new Processor(handler);

            Runnable serverThread = () -> {
                try {
                    int port = args.length > 4 ? Integer.parseInt(args[4]) : 9090;
                    TServerTransport serverTransport = new TServerSocket(port);
//                    TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
                    TServer server = new TThreadPoolServer(new TThreadPoolServer.Args(serverTransport).processor(processor));
                    server.serve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(serverThread).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
