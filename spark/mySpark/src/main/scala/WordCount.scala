import org.apache.spark.{SparkConf, SparkContext}

/**
  * Created by yangyibo on 16/11/22.
  */
object WordCount {
  def main(args: Array[String]) {
    val conf = new SparkConf().setAppName("WordCount")
    conf.setMaster("local")
    val sc = new SparkContext(conf)
    val rdd = sc.textFile("./input/readme.md")
    rdd.cache();
    //reduceByKey
    val wordCount = rdd.flatMap(_.split(' ')).map((_, 1)).reduceByKey(_ + _)
      //.map(x => (x._2,x._1)是将map 的key 和 value 换位置,_2为第二个参数。且 lookUp 只搜索key)
      //所以此处先换一次位置,(1,is) ,然后对 key 进行求和 ,然后再次修改权重进行显示(is,5)
            .map(x => (x._2,x._1)).sortByKey(false).map(x => (x._2,x._1))
    //输出文本的行数
    println("文本的行数:" + rdd.count())
    println("The 出现的次数:" + wordCount.lookup("The"))
    println("word count is work")
    wordCount.saveAsTextFile("./out/resulted")
  }
}
