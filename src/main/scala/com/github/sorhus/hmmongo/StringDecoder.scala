package com.github.sorhus.hmmongo

import com.github.sorhus.hmmongo.viterbi.Decoder

class StringDecoder extends Decoder[String] {

  override def apply(input: Array[Int]): String = input.mkString(",")
}
