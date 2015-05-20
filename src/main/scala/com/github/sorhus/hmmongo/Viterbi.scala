package com.github.sorhus.hmmongo

import scala.util.{Failure, Success, Try}

class Viterbi(hmm: HMM, T: Int, experimental: Boolean = false)
    extends Function[Array[Int], List[ViterbiResult]] {
  
  val impl: IViterbi = experimental match {
    case false => new ViterbiImpl(hmm, T)
    case true => new ExperimentalViterbiImpl(hmm, T)
  }
  
  override def apply(input: Array[Int]): List[ViterbiResult] = {
    Try(impl.getPath(input)) match {
      case Success(result) => result :: Nil
      case Failure(error) => Nil
    }
  }
}
