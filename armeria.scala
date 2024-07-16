//> using scala 3.4.1
//> using dep "com.softwaremill.sttp.tapir::tapir-core:1.10.13"
//> using dep "com.softwaremill.sttp.tapir::tapir-armeria-server:1.10.13"
//> using dep "com.outr::scribe:3.15.0"
//> using dep "com.outr::scribe-slf4j2:3.15.0"

import sttp.tapir.EndpointIO.annotations.jsonbody
import sttp.tapir.*
import sttp.shared.Identity
import sttp.tapir.server.armeria.ArmeriaFutureServerInterpreter
import com.linecorp.armeria.server.Server
import scala.concurrent.Future
import scala.concurrent.ExecutionContext.Implicits.global
import com.linecorp.armeria.common.SessionProtocol

val helloEndpoint = endpoint.get
  .in("hello")
  .in(stringJsonBody)
  .out(stringBody)
  .serverLogic { _ =>
    Future {
      Thread.sleep(1000)
      Right("Hello, Armeria!")
    }
  }

val handler = ArmeriaFutureServerInterpreter().toService(List(helloEndpoint))

@main def run = 
  Server
    .builder()
    .service(handler)
    .port(8080, SessionProtocol.HTTP)
    .build()
    .start()
    .join()