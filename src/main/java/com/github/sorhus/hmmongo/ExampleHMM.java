package com.github.sorhus.hmmongo;

public class ExampleHMM extends HMM {

    static double[] pi = {1.0/3, 1.0/3, 1.0/3};
    static double[][] A = {{0, 2.0/5, 3.0/5}, {1.0/5, 0, 4.0/5}, {0, 0, 1.0}};
    static double[][] B = {{0, 1.0}, {9.0/10, 1.0/10}, {1.0/2, 1.0/2}};

    public ExampleHMM() {
        super(ExampleHMM.pi,ExampleHMM.A,ExampleHMM.B);
    }

    public static void main(String[] args) {
        Viterbi viterbi = new Viterbi(new ExampleHMM(), 3);
        int[] observations = {0, 1, 0};
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
