import _root_.akka.actor.ActorSystem
import javax.servlet.ServletContext
import com.github.sorhus.hmmongo.TCRBViterbiServlet
import org.scalatra.LifeCycle

class ScalatraBootstrap extends LifeCycle {

  val system = ActorSystem()
//  val myActor = system.actorOf(Props[MyActor])

  override def init(context: ServletContext) {
//    context.mount(new TCRBViterbiServlet(system), "/*")
    context.mount(new TCRBViterbiServlet(system), "/")
  }

  override def destroy(context:ServletContext) {
    system.shutdown()
  }
}