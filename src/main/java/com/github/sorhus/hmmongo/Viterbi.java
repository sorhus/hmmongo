package com.github.sorhus.hmmongo;

public class Viterbi {

    private double[] pi;
    private double[][] A;
    private double[][] B;

    private double[][] PHI;
    private int[][] PSI;

    public Viterbi(double[] pi, double[][] A, double[][] B, int T) {
        this.pi = pi;
        this.A = A;
        this.B = B;
        this.PHI = new double[pi.length][];
        this.PSI = new int[pi.length][];
        for(int i = 0; i < pi.length; i++) {
            PHI[i] = new double[T];
            PSI[i] = new int[T];
        }
    }

    public int[] getPath(int[] observations) {
        initialise(observations);
        recurse(observations);
        return terminate(observations);
    }

    private void initialise(int[] observations) {
        for(int i = 0; i < observations.length; i++) {
            final int observation = observations[i];
            PHI[0][i] = pi[i] * B[i][observation];
        }
    }

    private void recurse(int[] observations) {
        for(int t = 1; t < observations.length; t++) {
            double[] phi = PHI[t];
            int[] psi = PSI[t];
            for(int j = 0; j < pi.length; j++) {
                double max = Integer.MIN_VALUE;
                int arg = -1;
                double b = 0.0;
                for(int i = 0; i < observations.length; i++) {
                    double v = PHI[t-1][i] * A[i][j];
                    if(v > max) {
                        max = v;
                        arg = i;
                        b = B[i][observations[t]];
                    }
                }
                phi[j] = max * b;
                psi[j] = arg;
            }
        }
    }

    private int[] terminate(int[] observations) {
        int path[] = new int[observations.length];
        double max = Integer.MIN_VALUE;
        double[] phi = PHI[observations.length - 1];
        for(int i = 0; i < pi.length; i++) {
            if(phi[i] > max) {
                max = phi[i];
                path[observations.length-1] = i;
            }
        }
        for(int t = observations.length - 2; t < 0; t--) {
            path[t] = PSI[t+1][path[t+1]];
        }
        return path;
    }

    public static void main(String[] args) {
        double[] pi = new double[] {1.0/3, 1.0/3, 1.0/3};
        double[][] A = {{0, 2.0/5, 3.0/5}, {1.0/5, 0, 4.0/5}, {0, 0, 1.0}};
        double[][] B = {{0, 1.0}, {9.0/10, 1.0/10}, {1.0/2, 1.0/2}};

        int[] observations = new int[] {0, 1, 0};
        int[] result = new Viterbi(pi, A, B, 3).getPath(observations);

        System.out.print("Optimal path is: ");
        for (int t = 0; t < result.length; t++) {
            System.out.print(t + " ");
        }
        System.out.println();
    }
}
