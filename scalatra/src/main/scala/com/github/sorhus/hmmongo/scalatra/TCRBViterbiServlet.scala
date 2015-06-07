package com.github.sorhus.hmmongo.scalatra

import java.io.InputStream

import _root_.akka.actor.ActorSystem
import com.github.sorhus.hmmongo.viterbi.result.{FullResult, Result}
import com.github.sorhus.hmmongo.viterbi._
import com.github.sorhus.hmmongo.hmm.{HMMBuilder, HMM}
import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import scala.concurrent.{Future, ExecutionContext}
import scala.util.Random

class TCRBViterbiServlet(system: ActorSystem) extends ScalatraServlet /*with ScalateSupport */with FutureSupport {

  val concurrency = 4
  //  val hmm: HMM = new HMMBuilder().fromFiles(s"$base/tcrb_pi.gz", s"$base/tcrb_A.gz", s"$base/tcrb_B.gz")
  val r: (String) => InputStream = getClass.getResourceAsStream
  val hmm: HMM = new HMMBuilder()
    .fromInputStreams(r("/example_pi.gz"), r("/example_A.gz"), r("/example_B.gz"))
    .adjacency
    .build

  val viterbiPool: Array[Viterbi[String, FullResult[String, String]]] =
    Range(0,concurrency).toArray.map { _ =>
      new ViterbiBuilder[String,String,FullResult[String,String]]()
        .withHMM(hmm)
        .withMaxObservationLength(101)
        .withObservationEncoder(new DNAEncoder)
        .withObservationDecoder(new DNADecoder)
        .withPathDecoder(new StringDecoder)
        .withResultFactoryClass("com.github.sorhus.hmmongo.viterbi.result.FullResultFactory")
        .withThreadSafety()
//        .withTimeLogging()
        .build
  }

  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/:dna") {
    new AsyncResult {
      val is: Future[Result] = Future {
        viterbiPool(Random.nextInt(concurrency))(params("dna"))
      }
    }
  }
}

