import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.sql.{SaveMode, SparkSession}

/**
  * Created by yangyibo on 16/12/5.
  */

case class Record(key: Int, value: String)

object RecordDemo {
  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[2]")
    val spark = SparkSession.builder().appName("RecordDemo").config(conf).getOrCreate()

    import spark.implicits._

    // s"val_$i"  s 默认是String 类型  $ 是引入变量
    val df = spark.createDataFrame((1 to 100).map(i => Record(i, s"val_$i")))
    df.createOrReplaceTempView("records")
    println("Result of SELECT *:")
    spark.sql("SELECT * FROM records").collect().foreach(println)

    println("——————————————————————————————————————————————————————")

    //head 取出head 的值,getLong是以Long的类型取出
    val count=spark.sql("select count(*) from  records").collect().head.getLong(0)
    println(s"COUNT(*): $count")


    println("——————————————————————————————————————————————————————")


    val rddFromSql=spark.sql("select  key , value from records  where key < 10")

    rddFromSql.collect().foreach(println)

    //rdd转换成map 对象
    rddFromSql.rdd.map(rows => s"Key: ${rows(0)}, value: ${rows(1)}").collect().foreach(println)

    println("——————————————————————————————————————————————————————")

    //过滤key大于10,然后按照 key降序排列,只查询 key列
    df.where($"key" >= 10).orderBy($"key".desc).select($"key").collect().foreach(println)


    println("——————————————————————————————————————————————————————")

    //"SaveMode.Overwrite" 如果数据或表已经存在，则用DataFrame数据覆盖之
    df.write.mode(SaveMode.Overwrite).parquet("src/main/resources/pair.parquet")

    //读取数据返回新的dataframe
    val parquetFile=spark.read.parquet("src/main/resources/pair.parquet")


    parquetFile.where($"key" === 10).select($"value",$"key".as("a")).collect().foreach(println)

    println("——————————————————————————————————————————————————————")


    // 创建临时视图
    parquetFile.createOrReplaceTempView("parquetFile")

    spark.sql("SELECT * FROM parquetFile").orderBy($"key".asc).collect().foreach(println)

    spark.stop()
  }

}
