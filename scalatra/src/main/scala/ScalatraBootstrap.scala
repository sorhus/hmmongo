import _root_.akka.actor.ActorSystem
import javax.servlet.ServletContext
import com.github.sorhus.hmmongo.scalatra.DNAViterbiServlet
import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {

  val system = ActorSystem()

  override def init(context: ServletContext) {
    val Array(pi,a,b,t) = Array("pi","A","B","T").map(context.getAttribute).map(_.toString)
    context.mount(new DNAViterbiServlet(pi, a, b, t.toInt, system), "/")
  }

  override def destroy(context:ServletContext) {
    system.shutdown()
  }
}