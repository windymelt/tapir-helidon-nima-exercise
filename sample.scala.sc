//> using scala 2.13
//> using toolkit default
//> using dep "org.apache.spark::spark-core:3.5.1"
//> using dep "org.apache.spark::spark-sql:3.5.1"
//> using jvm 11

import org.apache.spark.sql.SparkSession
import org.apache.spark.sql.functions.lit

val spark = SparkSession
  .builder()
  .appName("http-server-chart")
  .config("spark.master", "local") // 実行するマスターノードを指定するのが必須なのでlocalとする
  .getOrCreate()

val files: Seq[String] = args

val dfs = files.map { file =>
  println("loading file")

  val data = spark.read
    .format("json")
    .option("header", "true")
    .load(file)

  val df = data.filter("metric == 'http_req_duration'").select("data.value")
  // add new column with file name
  df.withColumn("file", lit(file))
}

// vertical combine all dataframes
val samples = 100_000
val combined = dfs.reduce(_ union _)
//val sampled = combined.sample(false, 2D*samples/combined.count).limit((combined.count/samples).toInt)
combined.write.format("parquet").save("http-server-chart.parquet")

println(combined.show())