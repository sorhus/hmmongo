package com.github.sorhus.hmmongo;

import java.io.*;
import java.util.*;
import java.util.zip.GZIPInputStream;

public class HMMFactory {

    public static HMM fromFiles(String piPath, String APath, String BPath) throws IOException {
        try(Scanner piScanner = getScanner(piPath);
            Scanner AScanner = getScanner(APath);
            Scanner BScanner = getScanner(BPath)) {

            double[] pi = readDoubleArray(piScanner);
            System.err.println("Read pi");
            double[][] A = readDoubleMatrix(AScanner, pi.length);
            System.err.println("Read A");
            double[][] B = readDoubleMatrix(BScanner, pi.length);
            System.err.println("Read B");
            return new HMM(pi, A, B);
        }
    }

    private static Scanner getScanner(String path) throws IOException {
        return new Scanner(new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(path)))));
    }

    private static double[][] readDoubleMatrix(Scanner sc, int n) throws IOException {
        double[][] result = new double[n][];
        result[0] = readDoubleArray(new Scanner(sc.nextLine()));
        for (int i = 1; i < n; i++) {
            System.err.print(".");
            result[i] = readDoubleArray(sc, result[i-1].length);
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
        List<Double> values = new LinkedList<>();
        while(sc.hasNext()) {
            values.add(sc.nextDouble());
        }
        double[] result = new double[values.size()];
        for (int i = 0; i < result.length; i++) {
            result[i] = values.remove(0);
        }
        return result;
    }
}