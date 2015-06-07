package com.github.sorhus.hmmongo.viterbi

class DNADecoder extends ObservationDecoder[String] {
  override def apply(t: Array[Int]): String = {
    t.map(Array('a','c','g','t')).mkString
  }
}
