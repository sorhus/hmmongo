package com.github.sorhus.hmmongo.viterbi

class StringDecoder extends PathDecoder[String] {

  override def apply(input: Array[Int]): String = input.mkString(",")
}
