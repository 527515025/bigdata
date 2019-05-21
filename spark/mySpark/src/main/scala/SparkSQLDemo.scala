import org.apache.spark.SparkConf
import org.apache.spark.sql.{Row, SparkSession}
import org.apache.spark.sql.types.{StringType, StructField, StructType}

/**
  * Created by yangyibo on 16/12/5.
  */
object SparkSQLDemo {

  //case class 定义了表的模式
  case class Person(name: String, age: Long)

  def main(args: Array[String]) {
    val conf = new SparkConf().setMaster("local[2]")
    val spark = SparkSession.builder().appName("SparkSQLDemo").config(conf).getOrCreate()

    //    runBasicDataFrame(spark)
    //    runDatasetCreation(spark)
    //    runInferSchema(spark)
    runProgrammaticSchema(spark)
    spark.stop()

  }

  def runBasicDataFrame(spark: SparkSession): Unit = {
    //df  is dataFrame
    val df = spark.read.json("src/main/resources/user.json")
    df.show()
    println("---------------------1------------------")
    df.printSchema()
    println("---------------------2------------------")

    df.select("name", "username").show()
    println("---------------------3------------------")

    //添加此标签可以使用$ 符号 传值计算需要加$ 符号
    import spark.implicits._
    df.select($"name", $"telephone" + 0.1).show()
    println("---------------------4------------------")

    df.filter($"username".contains("010")).show()
    println("---------------------5------------------")
    df.groupBy("username").count().show()


    println("---------------------6------------------")
    df.createOrReplaceTempView("people")
    val sqlDF = spark.sql("select * from people")
    sqlDF.show()

  }


  def runDatasetCreation(spark: SparkSession): Unit = {
    import spark.implicits._
    println("---------------------11------------------")

    val caseClassDS = Seq(Person("abel", 18)).toDS()
    caseClassDS.show()
    println("---------------------12------------------")

    val primitiveDS = Seq(1, 2, 3).toDS()
    primitiveDS.map(_ + 2).collect().foreach(println)

    println("---------------------13------------------")

    val userDS = spark.read.json("src/main/resources/user.json").as("user")
    userDS.show()

  }


  def runInferSchema(spark: SparkSession): Unit = {
    import spark.implicits._
    //使用反射来推断模式 来创建DF
    //不加.trim可能会因为有空格,无法转换成Int类型
    //    Spark SQL 的 Scala 接口支持将元素类型为 case class 的 RDD 自动转为 DataFrame。
    //    case class 定义了表的模式。case class 的参数名将变成对应列的列名。
    //    case class 可以嵌套，也可以包含复合类型，比如 Seqs 或 Arrays。
    //    元素为 case class 的 RDD 可以转换成 DataFrame 并可以注册为表进而执行 sql 语句查询。

    val txtDF = spark.sparkContext.textFile("./input/people.txt").map(_.split(",")).map(x => Person(x(0), x(1).trim.toInt)).toDF()
    txtDF.createTempView("user")

    println("---------------------21------------------")
    val teenAgeDF = spark.sql("select * from user where  age  between  19 and 30")
    teenAgeDF.map(x => "Name:" + x(0)).show()

    println("---------------------22------------------")
    teenAgeDF.map(x => "Name:" + x.getAs[String]("name")).show()


    println("---------------------23------------------")
    implicit val mapEncoder = org.apache.spark.sql.Encoders.kryo[Map[String, Any]]
    teenAgeDF.map(x => x.getValuesMap[Any](List("name", "age"))).collect().foreach(println)
  }


  def runProgrammaticSchema(spark: SparkSession): Unit = {
    import spark.implicits._
    //    编码指定模式  创建DF
    //    如果不能预先定义 case class（比如，每条记录都是字符串，不同的用户会使用不同的字段），那么可以通过以下三步来创建 DataFrame：
    //
    //    将原始 RDD 转换为 Row RDD
    //    根据步骤1中的 Row 的结构创建对应的 StructType 模式
    //    通过 SparkSession 提供的 createDataFrame 来把第2步创建的模式应用到第一步转换得到的 Row RDD
    val userRDD = spark.sparkContext.textFile("./input/people.txt")
    val schemaString = "name age"
    val filed = schemaString.split(" ").map(filedName => StructField(filedName, StringType, nullable = true))
    val schema = StructType(filed)
    val rowsRDD = userRDD.map(_.split(",")).map(x => Row(x(0), x(1).trim))
    //创建DF
    val userDF = spark.createDataFrame(rowsRDD, schema)
    userDF.createTempView("user")
    val result = spark.sql("select * from user ")
    println("---------------------31------------------")
    result.map(x => ("name :" + x(0), "age:" + x(1))).show()

  }
}
