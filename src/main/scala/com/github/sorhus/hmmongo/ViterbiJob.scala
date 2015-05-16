package com.github.sorhus.hmmongo

import com.twitter.scalding.typed.TypedPipe
import com.twitter.scalding.{TypedTsv, TextLine, Job, Args}

class ViterbiJob(args: Args) extends Job(args) {

  val hmm: HMM = HMMFactory.fromFiles(args("pi"), args("A"), args("B")).toLog
  println(s"Loaded hmm, n = ${hmm.n}")
  val encoder: (Array[Char]) => Array[Int] = (c: Array[Char]) => c.map(DNAEncoder.encode)
  val viterbi: (Array[Int]) => Array[Int] = new Viterbi(hmm, args("T").toInt).getPath

  TypedPipe.from(TextLine(args("input")))
    .map(_.toCharArray)
    .map(encoder)
    .map(viterbi)
    .map(_.mkString(","))
    .write(TypedTsv(args("output")))
}
