package com.github.sorhus.hmmongo;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

// TODO rewrite to use less memory
public class HMMFactory {

    public static HMM fromFiles(String piPath, String APath, String BPath) throws IOException {
        try(Scanner piScanner = getScanner(piPath);
            Scanner AScanner = getScanner(APath);
            Scanner BScanner = getScanner(BPath)) {

            double[] pi = readDoubleArray(piScanner);
            System.err.println("Read pi");
            double[][] Amatrix = readDoubleMatrix(AScanner, pi.length);
            List<Keyed> A = getKeyedListFromMatrix(Amatrix);
            System.err.println("Read A");
            double[][] B = readDoubleMatrix(BScanner, pi.length);
            System.err.println("Read B");
            return new HMM(pi, A, B, false);
        }
    }

    private static Scanner getScanner(String path) throws IOException {
        return new Scanner(new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path)))));
    }

    private static double[][] readDoubleMatrix(Scanner sc, int n) throws IOException {
        double[][] result = new double[n][];
        result[0] = readDoubleArray(new Scanner(sc.nextLine()));
        for (int i = 1; i < n; i++) {
            if(i % 100 == 0) {
                System.err.print(".");
            }
            result[i] = readDoubleArray(sc, result[i - 1].length);
        }
        System.err.println();
        return result;
    }

    private static double[] readDoubleArray(Scanner sc, int n) throws IOException {
        double[] result = new double[n];
        for (int i = 0; i < n; i++) {
            result[i] = sc.nextDouble();
        }
        return result;
    }

    private static double[] readDoubleArray(Scanner sc) throws IOException {
        java.util.List<Double> values = new LinkedList<>();
        while(sc.hasNext()) {
            values.add(sc.nextDouble());
        }
        double[] result = new double[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.remove(0);
        }
        return result;
    }

    public static HMM fromMatrixToAdjacency(HMM hmm) {
        double[] pi = new double[hmm.n];
        System.arraycopy(hmm.pi, 0, pi, 0, hmm.n);
        List<Keyed> A = new ArrayList<>(hmm.n);
        for (int i = 0; i < hmm.n; i++) {
            A.add(i, new KeyedMap());
        }
        for (int i = 0; i < hmm.n; i++) {
            for (int j : hmm.A.get(i).keys()) {
                final Double d = hmm.A.get(j).get(i);
                if(hmm.log ? d > Double.NEGATIVE_INFINITY : d > 0.0) {
                    A.get(j).put(i, d);
                }
            }
        }
        double[][] B = new double[hmm.n][];
        for (int i = 0; i <hmm.n; i++) {
            B[i] = new double[hmm.B[0].length];
            System.arraycopy(hmm.B[i], 0, B[i], 0, hmm.B[0].length);
        }
        return new HMM(pi, A, B, hmm.log);
    }

    public static List<Keyed> getKeyedListFromMatrix(double[][] A) {
        List<Keyed> result = new ArrayList<>(A.length);
        for (int j = 0; j < A.length; j++) {
            result.add(j, new KeyedList(A.length));
        }
        for (int i = 0; i < A.length; i++) {
            for (int j = 0; j < A.length; j++) {
                result.get(j).put(i, A[i][j]);
            }
        }
        return result;
    }

    public static HMM toLog(HMM hmm) {
        double[] pi_n = new double[hmm.n];
        List<Keyed> A_n = new ArrayList<>(hmm.n);
        double[][] B_n = new double[hmm.n][];
        for (int i = 0; i < hmm.n; i++) {
            pi_n[i] = Math.log(hmm.pi[i]);
            A_n.add(i, new KeyedList(hmm.n));
            for (Integer j : hmm.A.get(i).keys()) {
                A_n.get(i).put(j, Math.log(hmm.A.get(i).get(j)));
            }
            B_n[i] = new double[hmm.B[i].length];
            for (int j = 0; j < hmm.B[i].length; j++) {
                B_n[i][j] = Math.log(hmm.B[i][j]);
            }
        }
        return new HMM(pi_n, A_n, B_n, true);
    }

    public static HMM fromLog(HMM hmm) {
        double[] pi_n = new double[hmm.n];
        List<Keyed> A_n = new ArrayList<>(hmm.n);
        double[][] B_n = new double[hmm.n][];
        for (int i = 0; i < hmm.n; i++) {
            pi_n[i] = Math.log(hmm.pi[i]);
            A_n.add(i, new KeyedList(hmm.n));
            for (Integer j : hmm.A.get(i).keys()) {
                A_n.get(i).put(j, Math.exp(hmm.A.get(i).get(j)));
            }
            B_n[i] = new double[hmm.B[i].length];
            for (int j = 0; j < hmm.B[i].length; j++) {
                B_n[i][j] = Math.log(hmm.B[i][j]);
            }
        }
        return new HMM(pi_n, A_n, B_n, true);
    }

}