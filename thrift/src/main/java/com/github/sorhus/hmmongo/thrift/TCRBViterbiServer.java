package com.github.sorhus.hmmongo.thrift;

import com.github.sorhus.hmmongo.thrift.TCRBViterbiService.Processor;
import org.apache.thrift.server.TServer;
import org.apache.thrift.server.TSimpleServer;
import org.apache.thrift.transport.TServerSocket;
import org.apache.thrift.transport.TServerTransport;

public class TCRBViterbiServer {
    public static void main(String [] args) {
        try {
            TCRBViterbiService.Iface handler = new TCRBViterbiServiceImpl();
            Processor processor = new Processor(handler);

            Runnable simple = () -> {
                try {
                    TServerTransport serverTransport = new TServerSocket(9090);
                    TServer server = new TSimpleServer(new TServer.Args(serverTransport).processor(processor));
                    System.out.println("Starting the simple server...");
                    server.serve();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            };
            new Thread(simple).start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
