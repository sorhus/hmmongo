package com.github.sorhus.hmmongo.inference;

public class Normaliser {

    public static void normalise(double[] p, final boolean log) {
        if(log) exp(p);
        normalise(p);
        if(log) log(p);
    }

    public static void normalise(double[] p) {
        double sum = 0.0;
        for (int i = 0; i < p.length; i++) {
            sum += p[i];
        }
        for (int i = 0; i < p.length; i++) {
            p[i] /= sum;
        }
    }

    public static void log(double[] p) {
        for (int i = 0; i < p.length; i++) {
            p[i] = Math.log(p[i]);
        }
    }

    public static void exp(double[] p) {
        for (int i = 0; i < p.length; i++) {
            p[i] = Math.exp(p[i]);
        }
    }


}
