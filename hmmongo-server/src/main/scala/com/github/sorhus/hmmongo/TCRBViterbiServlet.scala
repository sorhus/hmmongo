package com.github.sorhus.hmmongo

import _root_.akka.actor.ActorSystem
import com.github.sorhus.hmmongo.hmm.{HMMBuilder, HMM}
import com.github.sorhus.hmmongo.viterbi._
import org.scalatra._

import scala.concurrent.{Future, ExecutionContext}
import scala.util.Random

class TCRBViterbiServlet(system: ActorSystem) extends ScalatraServlet with FutureSupport {

  val base = "hmmongo-server/src/main/resources"
  val concurrency = 2
  //  val hmm: HMM = new HMMBuilder().fromFiles(s"$base/tcrb_pi.gz", s"$base/tcrb_A.gz", s"$base/tcrb_B.gz")
  val hmm: HMM = new HMMBuilder().fromFiles(s"$base/example_pi.gz", s"$base/example_A.gz", s"$base/example_B.gz")
    .adjacency.asLog.build

  val viterbiPool: Array[Viterbi[String, String]] = Range(0,concurrency).toArray.map { _ =>
      new ViterbiBuilder(hmm, 101)
        .threadSafe
        .withEncoderDecoder(new DNAEncoder, new StringDecoder)
  }

  protected implicit def executor: ExecutionContext = system.dispatcher

  get("/:dna") {
    <html>
      <body>
        Hello World
      </body>
    </html>
//    new AsyncResult {
//      val is: Future[Result[String]] = Future {
//        viterbiPool(Random.nextInt(concurrency))(params("dna"))
//      }
//    }
  }
}

