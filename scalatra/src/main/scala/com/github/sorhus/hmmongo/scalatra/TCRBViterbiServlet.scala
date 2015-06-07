package com.github.sorhus.hmmongo.scalatra

import _root_.akka.actor.ActorSystem
import com.github.sorhus.hmmongo.viterbi.result.{FullResult, Result, BasicResult}
import com.github.sorhus.hmmongo.viterbi._
import com.github.sorhus.hmmongo.hmm.{HMMBuilder, HMM}
import org.scalatra._
import org.scalatra.scalate.ScalateSupport
import scala.concurrent.{Future, ExecutionContext}
import scala.util.Random

class TCRBViterbiServlet(system: ActorSystem) extends ScalatraServlet /*with ScalateSupport */with FutureSupport {

  val base = "scalatra/src/main/resources"
  val concurrency = 2
  //  val hmm: HMM = new HMMBuilder().fromFiles(s"$base/tcrb_pi.gz", s"$base/tcrb_A.gz", s"$base/tcrb_B.gz")
  val hmm: HMM = new HMMBuilder()
    .fromFiles(s"$base/example_pi.gz", s"$base/example_A.gz", s"$base/example_B.gz")
    .adjacency
    .build

  val viterbiPool: Array[Viterbi[String, String, FullResult[String, String]]] =
    Range(0,concurrency).toArray.map { _ =>
      new ViterbiBuilder[String,String,FullResult[String,String]]()
        .withHMM(hmm)
        .withMaxObservationLength(101)
        .withObservationEncoder(new DNAEncoder)
        .withObservationDecoder(new DNADecoder)
        .withPathDecoder(new StringDecoder)
        .withResultFactoryClass("com.github.sorhus.hmmongo.viterbi.result.FullResultFactory")
        .threadSafe
        .build
  }

  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/:dna") {
    new AsyncResult {
      val is: Future[Result[String,String]] = Future {
        viterbiPool(Random.nextInt(concurrency))(params("dna"))
      }
    }
  }
}

