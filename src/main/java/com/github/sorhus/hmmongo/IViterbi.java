package com.github.sorhus.hmmongo;

public interface IViterbi {
  ViterbiResult getPath(int[] observations) throws NoPossiblePathException;
}