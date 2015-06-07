package com.github.sorhus.hmmongo.viterbi

class StringDecoder extends Decoder[String] {

  override def apply(input: Array[Int]): String = input.mkString(",")
}
