package com.github.sorhus.hmmongo.thrift.client;

import com.github.sorhus.hmmongo.thrift.FullResult;
import com.github.sorhus.hmmongo.thrift.TCRBViterbiService;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

import java.util.concurrent.TimeUnit;

public class TCRBViterbiClient implements AutoCloseable {

    private final TTransport transport;
    private final TCRBViterbiService.Client client;

    public TCRBViterbiClient() throws TTransportException {
        transport = new TSocket("localhost", 9090);
        transport.open();
        TProtocol protocol = new TBinaryProtocol(transport);
        client = new TCRBViterbiService.Client(protocol);
    }

    public FullResult apply(String input) {
        try {
            return client.apply(input);
        } catch (TException e) {
            return null;
        }
    }

    @Override
    public void close() throws Exception {
        transport.close();
    }

    public static void main(String [] args) throws TTransportException {
        TCRBViterbiClient client = new TCRBViterbiClient();
        for (int i = 0; i < 10000; i++) {

            long t = System.nanoTime();
            FullResult result = client.apply("tcgttgcatcgatcgatcgatcgatcgtacgatcgatcgatcgactaca");
            System.out.println(TimeUnit.NANOSECONDS.toMicros(System.nanoTime() - t));
            System.out.println("Result is " + result);
        }
    }
}
