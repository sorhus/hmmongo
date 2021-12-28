import _root_.akka.actor.ActorSystem
import akka.dispatch.Futures

import javax.servlet.ServletContext
import com.github.sorhus.hmmongo.scalatra.DNAViterbiServlet
import org.scalatra.LifeCycle

import java.util.concurrent.TimeUnit
import scala.concurrent.Await
import scala.concurrent.duration.Duration

class ScalatraBootstrap extends LifeCycle {

  val system = ActorSystem()

  override def init(context: ServletContext) {
    val Array(pi,a,b,t) = Array("pi","A","B","T").map(context.getAttribute).map(_.toString)
    context.mount(new DNAViterbiServlet(pi, a, b, t.toInt, system), "/")
  }

  override def destroy(context: ServletContext) {
    Await.result(system.terminate(), Duration.apply(10, TimeUnit.SECONDS))
  }
}