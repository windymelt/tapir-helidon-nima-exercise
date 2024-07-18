//> using scala 3.4.1
//> using dep "com.softwaremill.sttp.tapir::tapir-core:1.10.13"
//> using dep "com.softwaremill.sttp.tapir::tapir-nima-server:1.10.13"
//> using dep "io.helidon.webserver:helidon-webserver-http2:4.0.10"
//.> using dep "io.helidon.webserver:helidon-webserver-access-log:4.0.10"
//> using dep "io.helidon.openapi:helidon-openapi:4.0.10"
//> using dep "com.outr::scribe:3.15.0"
//> using dep "com.outr::scribe-slf4j2:3.15.0"
//> using jvm 21+
import sttp.tapir.EndpointIO.annotations.jsonbody
//import io.helidon.webserver.accesslog.AccessLogFeature
import io.helidon.webserver.WebServer
import sttp.tapir.*
import sttp.shared.Identity
import sttp.tapir.server.nima.NimaServerInterpreter

val helloEndpoint = endpoint.get
  .in("hello")
  .in(stringJsonBody)
  .out(stringBody)
  .handleSuccess { _ =>
    "hello, Helidon Nima!"
  }

val handler = NimaServerInterpreter().toHandler(List(helloEndpoint))

WebServer
  .builder()
  // .addFeature(
  //   AccessLogFeature
  //     .builder()
  //     .commonLogFormat()
  //     .build()
  // )
  .routing { builder =>
    builder.any(handler)
    ()
  }
  .port(8080)
  .build()
  .start()
