package com.github.sorhus.hmmongo

import scala.util.{Failure, Success, Try}

class Viterbi(hmm: HMM, T: Int) extends Function[Array[Int], List[ViterbiResult]] {
  
  val impl = new ViterbiImpl(hmm, T)
  
  override def apply(input: Array[Int]): List[ViterbiResult] = {
    Try(impl.getPath(input)) match {
      case Success(result) => result :: Nil
      case Failure(error) => Nil
    }
  }
}
