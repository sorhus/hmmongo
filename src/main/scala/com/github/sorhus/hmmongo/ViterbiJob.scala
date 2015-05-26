package com.github.sorhus.hmmongo

import com.twitter.scalding.typed.TypedPipe
import com.twitter.scalding.{TypedTsv, TextLine, Job, Args}

class ViterbiJob(args: Args) extends Job(args) {

  val hmm: HMM = new HMMBuilder().fromFiles(args("pi"), args("A"), args("B"))
    .adjacency().asLog().build()
  val viterbi: Viterbi[String, Array[Int]] = new ViterbiBuilder(hmm, args("T").toInt, true)
    .withEncoder(new DNAEncoder(args.boolean("capitals")))

  TypedPipe.from(TextLine(args("input")))
    .map(viterbi.apply)
    .write(TypedTsv(args("output")))
}
