import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession

/**
  * Created by yangyibo on 16/11/24.
  */
object SQLDataSourceDemo {


  def main(args: Array[String]) {

    val sparkConf = new SparkConf().setAppName("SQLDataSourceDemo")
    sparkConf.setMaster("local")
    val spark = SparkSession.builder().appName("SQLDataSourceDemo").config(sparkConf).getOrCreate()
    runJDBCDataSource(spark)
    loadDataSourceFromeJson(spark)
    loadDataSourceFromeParquet(spark)
    runFromRDD(spark)
    spark.stop()
  }

  private def runJDBCDataSource(spark: SparkSession): Unit = {
    val jdbcDF = spark.read
      .format("jdbc")
      .option("url", "jdbc:mysql://localhost:3306/msm?user=root&password=admin")
      .option("dbtable", "sec_user") //必须写表名
      .load()
    //jdbcDF.select("id", "name", "telephone").write.format("parquet").save("src/main/resources/sec_users")
    //jdbcDF.select("username", "name", "telephone").write.format("json").save("src/main/resources/sec_users")

    //存储成为一张虚表user_abel
    jdbcDF.select("username", "name", "telephone").write.mode("overwrite").saveAsTable("user_abel")
    val jdbcSQl = spark.sql("select * from user_abel where name like '王%' ")
    jdbcSQl.show()
    jdbcSQl.write.format("json").save("./out/resulted")
  }

  private def loadDataSourceFromeJson(spark: SparkSession): Unit = {

    val jsonDF = spark.read.json("src/main/resources/user.json")

    jsonDF.printSchema()
    //创建临时视图
    jsonDF.createOrReplaceTempView("user")
    val namesDF = spark.sql("SELECT name FROM user WHERE name like '王%'")
    import spark.implicits._
    namesDF.map(attributes => "Name: " + attributes(0)).show()
    jsonDF.select("name").write.format("json").save("./out/resultedJSON")
  }

  private def loadDataSourceFromeParquet(spark: SparkSession): Unit = {

    val parquetDF = spark.read.load("src/main/resources/user.parquet")
    parquetDF.createOrReplaceTempView("user")
    val namesDF = spark.sql("SELECT name FROM user WHERE id > 1 ")
    namesDF.show()

    parquetDF.select("name").write.format("parquet").save("./out/resultedParquet")
  }

  private def runFromRDD(spark: SparkSession): Unit = {
    val otherPeopleRDD = spark.sparkContext.makeRDD(
      """{"name":"Yin","address":{"city":"Columbus","state":"Ohio"}}""" :: Nil)
    val otherPeople = spark.read.json(otherPeopleRDD)
    otherPeople.show()
  }

}
