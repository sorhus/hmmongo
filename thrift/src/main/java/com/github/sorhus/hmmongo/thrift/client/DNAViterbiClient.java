package com.github.sorhus.hmmongo.thrift.client;

import com.github.sorhus.hmmongo.thrift.DNAViterbiService;
import com.github.sorhus.hmmongo.thrift.FullResult;
import org.apache.thrift.TException;
import org.apache.thrift.protocol.TBinaryProtocol;
import org.apache.thrift.protocol.TProtocol;
import org.apache.thrift.transport.TSocket;
import org.apache.thrift.transport.TTransport;
import org.apache.thrift.transport.TTransportException;

public class DNAViterbiClient implements AutoCloseable {

    private final TTransport transport;
    private final DNAViterbiService.Client client;

    public DNAViterbiClient(String host, int port) throws TTransportException {
        transport = new TSocket(host, port);
        TProtocol protocol = new TBinaryProtocol(transport);
//        TProtocol protocol = new TCompactProtocol(transport);
//        TProtocol protocol = new TJSONProtocol(transport);
        client = new DNAViterbiService.Client(protocol);
        transport.open();
    }

    public DNAViterbiClient() throws TTransportException {
        this("localhost", 9090);
    }

    public FullResult apply(String input) {
        try {
            return client.apply(input);
        } catch (TException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {
        transport.close();
    }

    public static void main(String [] args) throws Exception {
        DNAViterbiClient client = new DNAViterbiClient();
        System.out.println(client.apply("tcgttgcatcgatcgatcgatcgatcgtacgatcgatcgatcgactaca"));
        client.close();
    }
}
