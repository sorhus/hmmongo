package com.github.sorhus.hmmongo;

public class MaybeFasterViterbiImpl implements Viterbi<int[], int[]> {

    final HMM hmm;
    final double[][] PHI;
    final int[][] PSI;

    public MaybeFasterViterbiImpl(HMM hmm, int T) {
        this.hmm = hmm;
        this.PHI = new double[T][];
        this.PSI = new int[T][];
        for(int i = 0; i < T; i++) {
            PHI[i] = new double[hmm.n];
            PSI[i] = new int[hmm.n];
        }
    }

    @Override
    public Result<int[]> apply(int[] observations) {
        initialise(observations[0]);
        recurse(observations);
        return terminate(observations.length);
    }

    protected void initialise(int first) {
        for(int i = 0; i < hmm.n; i++) {
            PHI[0][i] = hmm.log ?
                    hmm.pi[i] + hmm.B[i][first] :
                    hmm.pi[i] * hmm.B[i][first];
            PSI[0][i] = -1;
        }
    }

    protected void recurse(int[] observations) {
        for(int t = 1; t < observations.length; t++) {
            for(int j = 0; j < hmm.n; j++) {
                final double b = hmm.B[j][observations[t]];
                double max = Double.NEGATIVE_INFINITY;
                int argmax = -1;
                for (int i : hmm.A.get(j).keys()) {
                    final double v = hmm.log ?
                            PHI[t - 1][i] + hmm.A.get(j).get(i) :
                            PHI[t - 1][i] * hmm.A.get(j).get(i);
                    if (v > max) {
                        max = v;
                        argmax = i;
                    }
                }
                PHI[t][j] = hmm.log ?
                        max + b :
                        max * b;

                PSI[t][j] = PHI[t][j] == (hmm.log ? Double.NEGATIVE_INFINITY : 0.0) ?
                        -1 :
                        argmax;
            }
        }
    }

    protected Result<int[]> terminate(int observations) {
        int path[] = new int[observations];
        double max = Double.NEGATIVE_INFINITY;
        for(int i = 0; i < hmm.n; i++) {
            if(PHI[observations - 1][i] > max) {
                max = PHI[observations - 1][i];
                path[observations - 1] = i;
            }
        }
        for(int t = observations - 2; t > -1; t--) {
            if(path[t + 1] < 0) {
                return Result.NO_PATH;
            }
            path[t] = PSI[t + 1][path[t + 1]];
        }
        return new Result<>(path, max);
    }

}
