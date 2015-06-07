package com.github.sorhus.hmmongo.viterbi

import scala.util.{Failure, Success, Try}

class DNAEncoder extends Encoder[String] {

  def apply(input: String): Array[Int] = {
    Try(input.toCharArray.map(encode)) match {
      case Success(result) => result
      case Failure(error) => Array.empty
    }
  }

  def encode(ch: Char): Int = ch match {
    case 'a' | 'A' => 0
    case 'c' | 'C' => 1
    case 'g' | 'G' => 2
    case 't' | 'T' => 3
    case  _  => throw new IllegalArgumentException
  }

}
