package com.github.sorhus.hmmongo.httpcore;

import java.io.File;
import java.io.IOException;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.net.URLDecoder;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.ConnectionClosedException;
import org.apache.http.ExceptionLogger;
import org.apache.http.HttpConnection;
import org.apache.http.HttpEntity;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpException;
import org.apache.http.HttpRequest;
import org.apache.http.HttpResponse;
import org.apache.http.HttpStatus;
import org.apache.http.MethodNotSupportedException;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.FileEntity;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.bootstrap.HttpServer;
import org.apache.http.impl.bootstrap.ServerBootstrap;
import org.apache.http.protocol.HttpContext;
import org.apache.http.protocol.HttpCoreContext;
import org.apache.http.protocol.HttpRequestHandler;
import org.apache.http.ssl.SSLContexts;
import org.apache.http.util.EntityUtils;


public class App {
    public static void main(String[] args) throws Exception {
//        if (args.length < 1) {
//            System.err.println("Please specify document root directory");
//            System.exit(1);
//        }
        // Document root directory
//        String docRoot = args[0];
        int port = 8080;
//        if (args.length >= 2) {
//            port = Integer.parseInt(args[1]);
//        }

//        SSLContext sslcontext = null;
//        if (port == 8443) {
//             Initialize SSL context
//            URL url = HttpFileServer.class.getResource("/my.keystore");
//            if (url == null) {
//                System.out.println("Keystore not found");
//                System.exit(1);
//            }
//            sslcontext = SSLContexts.custom()
//                    .loadKeyMaterial(url, "secret".toCharArray(), "secret".toCharArray())
//                    .build();
//        }

        SocketConfig socketConfig = SocketConfig.custom()
                .setSoTimeout(15000)
                .setTcpNoDelay(true)
                .build();

        final HttpServer server = ServerBootstrap.bootstrap()
                .setListenerPort(port)
                .setServerInfo("Test/1.1")
                .setSocketConfig(socketConfig)
//                .setSslContext(sslcontext)
//                .setExceptionLogger(new StdErrorExceptionLogger())
                .registerHandler("*", new TCRBHandler())
                .create();

        server.start();
        server.awaitTermination(Long.MAX_VALUE, TimeUnit.DAYS);

        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                server.shutdown(5, TimeUnit.SECONDS);
            }
        });
    }
}