import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yangyibo on 16/11/22.
  */
object MySpark {

  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("MySpark")
    val sc = new SparkContext(conf)
    val rdd = sc.parallelize(List(1, 2, 3, 4, 5, 6)).map(_ * 3)
    val mappedRDD = rdd.filter(_ > 10).collect()
    //对集合求和
    println(rdd.reduce(_ + _))
    //输出大于10的元素
    for (arg <- mappedRDD)
      print(arg + " ")
    println()
    println("math is work")
    val a = sc.parallelize(List((1, 2), (3, 4), (3, 6)))
    println("reduceByKey :" + a.reduceByKey((x, y) => x + y).collect)

  }
}
