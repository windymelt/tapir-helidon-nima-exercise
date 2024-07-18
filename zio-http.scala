//> using scala 3.4.1
//> using dep "com.softwaremill.sttp.tapir::tapir-core:1.10.13"
//> using dep "com.outr::scribe:3.15.0"
//> using dep "com.outr::scribe-slf4j2:3.15.0"
//> using dep "com.softwaremill.sttp.tapir::tapir-zio-http-server:1.10.14"

import sttp.tapir.EndpointIO.annotations.jsonbody
//import sttp.tapir.*
import sttp.shared.Identity
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import sttp.tapir.server.ziohttp.ZioHttpInterpreter
import zio.http.{Request, Response, Routes}
import zio.*
import sttp.tapir.ztapir.*
import zio.http.*
import sttp.tapir.PublicEndpoint

object ZIOServer extends ZIOAppDefault {
  val helloEndpoint: PublicEndpoint[String, Unit, String, Any] = sttp.tapir.ztapir.endpoint.get
  .in("hello")
  .in(stringJsonBody)
  .out(stringBody)

  val handler = ZioHttpInterpreter().toHttp(helloEndpoint.zServerLogic { _ =>
    ZIO.succeed("hello, ZIO!")
  })
  def run = Server.serve(handler).provide(Server.default)
}