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
        this.PHI = new double[T][];
        this.PSI = new int[T][];
        for(int i = 0; i < T; i++) {
            PHI[i] = new double[pi.length];
            PSI[i] = new int[pi.length];
        }
    }

    public int[] getPath(int[] observations) {
        initialise(observations);
        recurse(observations);
        return terminate(observations);
    }

    private void initialise(int[] observations) {
        for(int i = 0; i < pi.length; i++) {
            PHI[0][i] = pi[i] * B[i][observations[0]];
            PSI[0][i] = -1;
        }
    }

    private void recurse(int[] observations) {
        for(int t = 1; t < observations.length; t++) {
            for(int j = 0; j < pi.length; j++) {
                double max = Integer.MIN_VALUE;
                int argmax = -1;
                double b = 0.0;
                for(int i = 0; i < pi.length; i++) {
                    final double v = PHI[t-1][i] * A[i][j];
                    if(v > max) {
                        max = v;
                        argmax = i;
                        b = B[j][observations[t]];
                    }
                }
                PHI[t][j] = max * b;
                PSI[t][j] = PHI[t][j] == 0.0 ? -1 : argmax;
            }
        }
    }

    private int[] terminate(int[] observations) {
        int path[] = new int[observations.length];
        double max = Integer.MIN_VALUE;
        for(int i = 0; i < pi.length; i++) {
            if(PHI[observations.length - 1][i] > max) {
                max = PHI[observations.length - 1][i];
                path[observations.length - 1] = i;
            }
        }
        for(int t = observations.length - 2; t > -1; t--) {
            path[t] = PSI[t+1][path[t+1]];
        }
        return path;
    }

    public static void main(String[] args) {
        double[] pi = new double[] {1.0/3, 1.0/3, 1.0/3};
        double[][] A = {{0, 2.0/5, 3.0/5}, {1.0/5, 0, 4.0/5}, {0, 0, 1.0}};
        double[][] B = {{0, 1.0}, {9.0/10, 1.0/10}, {1.0/2, 1.0/2}};

        Viterbi viterbi = new Viterbi(pi, A, B, 3);


        int[] observations = new int[] {0, 1, 0};

        int[] result = viterbi.getPath(observations);

        System.out.println();
        System.out.print("Optimal path is: ");
        for (int t = 0; t < result.length; t++) {
            System.out.print(result[t] + " ");
        }
        System.out.println();
        System.out.println();
        System.out.println("PHI:");
        for(double[] phi : viterbi.PHI) {
            for(double p : phi) {
                System.out.print(p + " ");
            }
            System.out.println();
        }
        System.out.println();
        System.out.println("PSI:");
        for(int[] psi : viterbi.PSI) {
            for(int p : psi) {
                System.out.print(p + " ");
            }
            System.out.println();
        }
    }
}
