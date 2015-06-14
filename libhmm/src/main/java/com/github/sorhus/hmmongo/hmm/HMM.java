package com.github.sorhus.hmmongo.hmm;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Scanner;
import java.util.zip.GZIPInputStream;

/**
 * Hidden Markov Model defined by the initial distribution {@code pi},
 * the state transition distributions {@code A} and the emission
 * distributions {@code B}.
 *
 * States and emissions are both assumed to be encoded as consecutive
 * nonnegative integers.
 * Distributions are expressed as log probabilities
 */
public class HMM {

    /**
     * The initial distribution.
     * {@code pi[i]} is the prior to start in state {@code i}.
     */
    final public double[] pi;

    /**
     * The state transition distributions.
     * {@code A.get(j).get(i)} is the prior to go from state {@code i} to {@code j}.
     */
    final public List<Keyed> A;

    /**
     * The emission distributions
     * {@code B[i][j]} is the prior to emit {@code j} when in state {@code i}.
     */
    final public double[][] B;

    /**
     * The size of the state space.
     */
    final public int n;

    public HMM(double[] pi, List<Keyed> A, double[][] B) {
        this.pi = pi;
        this.A = A;
        this.B = B;
        this.n = pi.length;
        verify();
    }

    private void verify() {
        if (A.size() != n || B.length != n) {
            throw new IllegalArgumentException();
        }
        int m = B[0].length;
        for (double[] b : B) {
            if (b.length != m) {
                throw new IllegalArgumentException();
            }
        }
    }

    /**
     * Builder for constructing an {@code HMM}.
     * Input is assumed to be in matrix format, either in probabilities
     * or log probabilities.
     */
    public static class Builder {

        private InputStream piIS, AIS, BIS;
        private double[] pi;
        private double[][] rawA, B;
        private boolean inLog = false;
        private boolean adjacency = false;
        private KeyedFactory keyedFactory;

        public Builder() {}

        public Builder fromInputStreams(InputStream pi, InputStream A, InputStream B, boolean log) {
            this.piIS = pi;
            this.AIS = A;
            this.BIS = B;
            this.inLog = log;
            return this;
        }

        public Builder fromInputStreams(InputStream pi, InputStream A, InputStream B) {
            return fromInputStreams(pi, A, B, false);
        }


        public Builder fromFiles(String pi, String A, String B) throws FileNotFoundException {
            return fromFiles(pi, A, B, false);
        }

        public Builder fromFiles(String pi, String A, String B, boolean log) throws FileNotFoundException {
            this.piIS = new FileInputStream(pi);
            this.AIS = new FileInputStream(A);
            this.BIS = new FileInputStream(B);
            this.inLog = log;
            return this;
        }

        public Builder fromArrays(double[] pi, double[][] A, double[][] B) {
            return fromArrays(pi, A, B, false);
        }


        public Builder fromArrays(double[] pi, double[][] A, double[][] B, boolean log) {
            this.pi = pi;
            this.rawA = A;
            this.B = B;
            this.inLog = log;
            return this;
        }

        /**
         * Indicates that {@code A} should be represented using adjacency
         * lists rather than a dense matrix.
         */
        public Builder adjacency() {
            this.adjacency = true;
            return this;
        }

        public HMM build() throws IOException {
            List<Keyed> A;
            if(pi != null && rawA != null && B != null) {
                pi = transfromArray(pi);
                A = transfromA(rawA);
                B = transfromB(B);
            } else {
                try(Scanner sc = getScanner(piIS)) {
                    pi = readArray(sc);
                }
                try(Scanner sc = getScanner(AIS)) {
                    A = readA(sc, pi.length);
                }
                try(Scanner sc = getScanner(BIS)) {
                    B = readB(sc, pi.length);
                }
            }
            return new HMM(pi, A, B);
        }

        private Scanner getScanner(InputStream is) throws IOException {
            return new Scanner(new BufferedReader(new InputStreamReader(new GZIPInputStream(is))));
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
            return !adjacency || d > Double.NEGATIVE_INFINITY;
        }

        private double getValue(Scanner sc) {
            return getValue(sc.nextDouble());
        }

        private double getValue(double value) {
            return inLog ? value : Math.log(value);
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
}