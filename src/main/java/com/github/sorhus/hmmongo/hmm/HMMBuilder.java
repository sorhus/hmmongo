package com.github.sorhus.hmmongo.hmm;

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
    private double[] pi;
    private double[][] rawA, B;
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

    public HMMBuilder fromArrays(double[] pi, double[][] A, double[][] B) {
        return fromArrays(pi, A, B, false);
    }


    public HMMBuilder fromArrays(double[] pi, double[][] A, double[][] B, boolean log) {
        this.pi = pi;
        this.rawA = A;
        this.B = B;
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
        List<Keyed> A;
        if(pi != null && rawA != null && B != null) {
            pi = transfromArray(pi);
            A = transfromA(rawA);
            B = transfromB(B);
        } else {
            try(Scanner sc = getScanner(piPath)) {
                pi = readArray(sc);
            }
            try(Scanner sc = getScanner(APath)) {
                A = readA(sc, pi.length);
            }
            try(Scanner sc = getScanner(BPath)) {
                B = readB(sc, pi.length);
            }
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

    private double[] transfromArray(double[] row) {
        double[] result = new double[row.length];
        for (int i = 0; i < row.length; i++) {
            result[i] = getValue(row[i]);
        }
        return result;
    }

    private List<Keyed> readA(Scanner sc, int n) throws IOException {
        keyedFactory = adjacency ? new KeyedMapFactory() : new KeyedListFactory();
        List<Keyed> A = new ArrayList<>(n);
        for (int j = 0; j < n; j++) {
            A.add(j, keyedFactory.create(n));
        }
        for (int i = 0; i < n; i++) {
            final double[] row = readArray(sc, n);
            for (int j = 0; j < n; j++) {
                if(populate(row[j])) {
                    A.get(j).put(i, row[j]);
                }
            }
        }
        return A;
    }

    private List<Keyed> transfromA(double[][] rawA) {
        keyedFactory = adjacency ? new KeyedMapFactory() : new KeyedListFactory();
        List<Keyed> A = new ArrayList<>(rawA.length);
        for (int j = 0; j < rawA.length; j++) {
            A.add(j, keyedFactory.create(rawA.length));
        }
        for (int i = 0; i < rawA.length; i++) {
            for (int j = 0; j < rawA.length; j++) {
                final double d = getValue(rawA[i][j]);
                if(populate(d)) {
                    A.get(j).put(i, d);
                }
            }
        }
        return A;
    }

    private double[][] readB(Scanner sc, int n) throws IOException {
        double[][] result = new double[n][];
        result[0] = readArray(new Scanner(sc.nextLine()));
        for (int i = 1; i < n; i++) {
            result[i] = readArray(sc, result[i - 1].length);
        }
        return result;
    }

    private double[][] transfromB(double[][] B) {
        double[][] result = new double[B.length][];
        for (int i = 0; i < B.length; i++) {
            result[i] = transfromArray(B[i]);
        }
        return result;
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
        return getValue(sc.nextDouble());
    }

    private double getValue(double value) {
        if(!inLog && outLog) {
            return Math.log(value);
        } else if(inLog && !outLog) {
            return Math.exp(value);
        } else {
            return value;
        }
    }


    private interface KeyedFactory {
        Keyed create(int n);
    }
    private class KeyedListFactory implements KeyedFactory {
        @Override
        public Keyed create(int n) {
            return new KeyedList(n);
        }
    }
    private class KeyedMapFactory implements KeyedFactory {
        @Override
        public Keyed create(int n) {
            return new KeyedMap();
        }
    }
}