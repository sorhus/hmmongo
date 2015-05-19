package com.github.sorhus.hmmongo;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

public class HMMBuilder {

    private String piPath, APath, BPath;
    private boolean inLog, outLog = false;
    private boolean adjacency = false;
    private KeyedFactory keyedFactory;

    public HMMBuilder() {}

    public HMMBuilder fromFiles(String piPath, String APath, String BPath) {
        return fromFiles(piPath, APath, BPath, false);
    }

    public HMMBuilder fromFiles(String piPath, String APath, String BPath, boolean log) {
        this.piPath = piPath;
        this.APath = APath;
        this.BPath = BPath;
        this.inLog = log;
        return this;
    }


    public HMMBuilder adjacency() {
        this.adjacency = true;
        return this;
    }

    public HMMBuilder asLog() {
        this.outLog = true;
        return this;
    }

    public HMM build() throws IOException {
        keyedFactory = adjacency ? new KeyedMapFactory() : new KeyedListFactory();
        double[] pi;
        List<Keyed> A;
        double[][] B;
        try(Scanner sc = getScanner(piPath)) {
             pi = readArray(sc);
        }
        try(Scanner sc = getScanner(APath)) {
            A = readA(sc, pi.length);
        }
        try(Scanner sc = getScanner(BPath)) {
            B = readB(sc, pi.length);
        }
        return new HMM(pi, A, B, outLog);
    }

    private Scanner getScanner(String path) throws IOException {
        return new Scanner(new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path)))));
    }

    private double[] readArray(Scanner sc, int n) throws IOException {
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = getValue(sc);
        }
        return result;
    }

    private double[] readArray(Scanner sc) throws IOException {
        java.util.List<Double> values = new LinkedList<>();
        while(sc.hasNext()) {
            values.add(getValue(sc));
        }
        double[] result = new double[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.remove(0);
        }
        return result;
    }

    private double[][] readB(Scanner sc, int n) throws IOException {
        double[][] result = new double[n][];
        result[0] = readArray(new Scanner(sc.nextLine()));
        for (int i = 1; i < n; i++) {
            result[i] = readArray(sc, result[i - 1].length);
        }
        return result;
    }

    private List<Keyed> readA(Scanner sc, int n) throws IOException {
        List<Keyed> A = new ArrayList<>(n);
        for (int j = 0; j < n; j++) {
            A.add(j, keyedFactory.get(n));
        }
        for (int i = 0; i < n; i++) {
            if(i % 100 == 0) {
                System.err.print(".");
            }
            final double[] row = readArray(sc, n);
            for (int j = 0; j < n; j++) {
                if(populate(row[j])) {
                    A.get(j).put(i, row[j]);
                } else {
                }
            }
        }
        return A;
    }

    private boolean populate(double d) {
        if(adjacency) {
            return outLog ?
                    d > Double.NEGATIVE_INFINITY :
                    d > 0.0;
        } else {
            return true;
        }
    }

    private double getValue(Scanner sc) {
        double value = sc.nextDouble();
        if(!inLog && outLog) {
             return Math.log(value);
        } else if(inLog && !outLog) {
             return Math.exp(value);
        } else {
            return value;
        }
    }

    private interface KeyedFactory {
        Keyed get(int n);
    }
    private class KeyedListFactory implements KeyedFactory {
        @Override
        public Keyed get(int n) {
            return new KeyedList(n);
        }
    }
    private class KeyedMapFactory implements KeyedFactory {
        @Override
        public Keyed get(int n) {
            return new KeyedMap();
        }
    }
}