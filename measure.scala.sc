//> using scala 3.4
//> using toolkit default
//> using dep "com.outr::scribe:3.15.0"

import scribe.*

val files = Seq("http4s-ember.scala", "armeria.scala", "helidon.scala.sc", "zio-http.scala", "pekko-http.scala")

val result = files.map { file =>
  scribe.info(s"Path: $file")
  scribe.info("Spawning server...")
  val server = os.proc("scala-cli", "run", file).spawn()
  Thread.sleep(5000)
  scribe.info("Running k6")
  val outFile = s"$file.json"
  val k6 = os.proc("k6", "run", "--out", s"json=$outFile", "script.js").call()
  server.destroy()
  outFile
}

scribe.info("Benchmark completed", data("files", files), data("result", result))