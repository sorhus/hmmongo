package com.github.sorhus.hmmongo

import com.github.sorhus.hmmongo.hmm.{HMM, HMMBuilder}
import com.github.sorhus.hmmongo.viterbi.{ViterbiBuilder, Viterbi}
import com.twitter.scalding.typed.TypedPipe
import com.twitter.scalding.{TypedTsv, TextLine, Job, Args}

class DNAViterbiJob(args: Args) extends Job(args) {

  val hmm: HMM = new HMMBuilder().fromFiles(args("pi"), args("A"), args("B"))
    .adjacency().asLog().build()
  val viterbi: Viterbi[String, String] = new ViterbiBuilder(hmm, args("T").toInt)
    .withEncoderDecoder(new DNAEncoder, new StringDecoder)

  TypedPipe.from(TextLine(args("input")))
    .map(viterbi.apply)
    .write(TypedTsv(args("output")))
}
