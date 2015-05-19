package com.github.sorhus.hmmongo

import com.twitter.scalding.typed.TypedPipe
import com.twitter.scalding.{TypedTsv, TextLine, Job, Args}
import HMMFactory._

class ViterbiJob(args: Args) extends Job(args) {

  val hmm: HMM = new HMMBuilder().fromFiles(args("pi"), args("A"), args("B"))
    .adjacency().asLog().build()
  println(s"Loaded hmm, n = ${hmm.n}")
  val encoder = (c: Array[Char]) => c.map(DNAEncoder.encode)
  val viterbi = (a: Array[Int]) => new Viterbi(hmm, args("T").toInt).getPath(a).path

  TypedPipe.from(TextLine(args("input")))
    .map(_.toCharArray)
    .map(encoder)
    .map(viterbi)
    .map(_.mkString(","))
    .write(TypedTsv(args("output")))
}
