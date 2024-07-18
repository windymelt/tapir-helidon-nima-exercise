//> using scala 3.4.1
//> using dep "com.softwaremill.sttp.tapir::tapir-core:1.10.13"
//> using dep "com.outr::scribe:3.15.0"
//> using dep "com.outr::scribe-slf4j2:3.15.0"
//> using dep "com.softwaremill.sttp.tapir::tapir-pekko-http-server:1.10.14"
//> using dep "org.apache.pekko::pekko-http:1.0.1"
//> using dep "org.apache.pekko::pekko-actor-typed:1.0.3"

import scala.concurrent.Future
import sttp.tapir.EndpointIO.annotations.jsonbody
import sttp.tapir.*
import sttp.shared.Identity
import sttp.tapir.PublicEndpoint
import sttp.tapir.server.pekkohttp.PekkoHttpServerInterpreter
import org.apache.pekko.http.scaladsl.server.Route
import org.apache.pekko
import pekko.actor.typed.ActorSystem
import pekko.actor.typed.scaladsl.Behaviors
import pekko.http.scaladsl.Http
import pekko.http.scaladsl.model._
import pekko.http.scaladsl.server.Directives._
import scala.concurrent.ExecutionContextExecutor

object PekkoHttpServer extends App {
  implicit val system: ActorSystem[Nothing] = ActorSystem(Behaviors.empty, "my-system")
  implicit val executionContext: ExecutionContextExecutor = system.executionContext
  val helloEndpoint: PublicEndpoint[String, Unit, String, Any] = endpoint.get
  .in("hello")
  .in(stringJsonBody)
  .out(stringBody)

  val handler = PekkoHttpServerInterpreter().toRoute(helloEndpoint.serverLogic { _ =>
    Future.successful(Right("hello, Pekko!"))
  })
  val bindingFuture = Http().newServerAt("localhost", 8080).bind(handler)
  // bindingFuture
  //     .flatMap(_.unbind()) // trigger unbinding from the port
  //     .onComplete(_ => system.terminate())
}