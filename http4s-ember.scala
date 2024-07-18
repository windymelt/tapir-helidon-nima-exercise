//> using scala 3.4.1
//> using dep "com.softwaremill.sttp.tapir::tapir-core:1.10.13"
//> using dep "com.softwaremill.sttp.tapir::tapir-http4s-server:1.10.14"
//> using dep "org.http4s::http4s-ember-server:0.23.27"
//> using dep "com.outr::scribe:3.15.0"
//> using dep "com.outr::scribe-slf4j2:3.15.0"

import sttp.tapir.EndpointIO.annotations.jsonbody
import sttp.tapir.*
import sttp.tapir.server.http4s.Http4sServerInterpreter
import cats.effect.IO
import org.http4s.HttpRoutes
import org.http4s.ember.server.*


object Http4sMain extends cats.effect.IOApp.Simple:
  val helloEndpoint = endpoint.get
  .in("hello")
  .in(stringJsonBody)
  .out(stringBody)
  .serverLogic { _ =>
    IO.pure(Right("hello, Ember!"))
  }

  val handler = Http4sServerInterpreter[IO]().toRoutes(List(helloEndpoint))

  def run: IO[Unit] =
    EmberServerBuilder.default[IO]
      .withHttpApp(handler.orNotFound)
      .build
      .use(_ => IO.never)