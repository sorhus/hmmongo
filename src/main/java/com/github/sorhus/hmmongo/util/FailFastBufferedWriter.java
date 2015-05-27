package com.github.sorhus.hmmongo.util;

import java.io.*;

public class FailFastBufferedWriter implements AutoCloseable {

    final private BufferedWriter bw;

    public FailFastBufferedWriter(String output)
            throws FileNotFoundException, UnsupportedEncodingException {
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(output), "utf-8"));
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
