package com.github.sorhus.hmmongo

import com.twitter.scalding.typed.TypedPipe
import com.twitter.scalding.{TypedTsv, TextLine, Job, Args}

class ViterbiJob(args: Args) extends Job(args) {

  val hmm: HMM = new HMMBuilder().fromFiles(args("pi"), args("A"), args("B"))
    .adjacency().asLog().build()
  val encoder = new DNAEncoder(args.boolean("input-capitals"))
  val viterbi: Viterbi = new Viterbi(hmm, args("T").toInt)

  TypedPipe.from(TextLine(args("input")))
    .flatMap(encoder)
    .flatMap(viterbi)
    .write(TypedTsv(args("output")))
}
