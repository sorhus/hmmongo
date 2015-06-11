package com.github.sorhus.hmmongo

import com.github.sorhus.hmmongo.hmm.HMM
import com.github.sorhus.hmmongo.viterbi.{StringDecoder, DNADecoder, DNAEncoder, Viterbi}
import com.github.sorhus.hmmongo.viterbi.result.FullResult
import com.twitter.scalding.typed.TypedPipe
import com.twitter.scalding.{TypedTsv, TextLine, Job, Args}

class DNAViterbiJob(args: Args) extends Job(args) {

  val hmm: HMM = new HMM.Builder().fromFiles(args("pi"), args("A"), args("B"))
    .adjacency()
    .build()
  val viterbi: Viterbi[String, FullResult[String, String]] =
    new Viterbi.Builder[String, String, FullResult[String, String]]()
    .withHMM(hmm)
    .withMaxObservationLength(args("T").toInt)
    .withObservationEncoder(new DNAEncoder)
    .withObservationDecoder(new DNADecoder)
    .withPathDecoder(new StringDecoder)
    .withResultFactoryClass("com.github.sorhus.hmmongo.viterbi.result.FullResultFactory")
    .withThreadSafety()
//    .withTimeLogging()
    .build()

  TypedPipe.from(TextLine(args("input")))
    .map(viterbi.apply)
    .write(TypedTsv(args("output")))
}
