package com.github.sorhus.hmmongo

import scala.util.{Failure, Success, Try}

class DNAEncoder(capitals: Boolean = false) extends Function[String, List[Array[Int]]] {

  override def apply(input: String): List[Array[Int]] = {
    Try(input.toCharArray.map(encode)) match {
      case Success(result) => result :: Nil
      case Failure(error) => Nil
    }
  }

  def encode(input: Char): Int = {
    val ch = if(capitals) input.toLower else input
    ch match {
      case 'a' => 0
      case 'c' => 1
      case 'g' => 2
      case 't' => 3
      case  _  => throw new IllegalArgumentException
    }
  }
}
