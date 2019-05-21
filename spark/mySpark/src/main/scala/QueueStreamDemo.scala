import java.sql.{DriverManager, ResultSet}

import scala.collection.mutable.Queue
import org.apache.spark.{SparkConf, SparkContext}
import org.apache.spark.rdd.RDD
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by yangyibo on 16/11/23.
  * RDD队列的作为streaming的输入流
  */
object QueueStreamDemo {
  def main(args: Array[String]) {

    //    StreamingExamples.setStreamingLogLevels()
    val sparkConf = new SparkConf().setAppName("QueueStreamAndJDBCDemo")
    //    sparkConf.setMaster("local")
    // Create the context  Seconds how long get again
    val ssc = new StreamingContext(sparkConf, Seconds(3))

    // Create the queue through which RDDs can be pushed to
    // a QueueInputDStream
    var rddQueue = new Queue[RDD[String]]()
    // Create the QueueInputDStream and use it do some processing
    val inputStream = ssc.queueStream(rddQueue)
    val mappedStream = inputStream.map(x => (x + "a", 1))
    val reducedStream = mappedStream.reduceByKey(_ + _)
      .map(x => (x._2, x._1)).filter((x) => x._1 > 1).filter((x) => x._2.equals("testa"))
    reducedStream.print()
    reducedStream.saveAsTextFiles("./spark/mySpark/out/resulted")
    ssc.start()

    val seq = conn()
    println(Seq)
    //Create and push some RDDs into rddQueue
    for (i <- 1 to 3) {

      rddQueue.synchronized {
        rddQueue += ssc.sparkContext.makeRDD(seq, 10)
      }
      Thread.sleep(3000)
    }
    ssc.stop()
  }


  def conn(): Seq[String] = {
    val user = "root"
    val password = "admin"
    val host = "192.168.100.232"
    val database = "msm"
    val conn_str = "jdbc:mysql://" + host + ":3306/" + database + "?user=" + user + "&password=" + password
    //classOf[com.mysql.jdbc.Driver]
    Class.forName("com.mysql.jdbc.Driver").newInstance();
    val conn = DriverManager.getConnection(conn_str)
    var setName = Seq("")
    try {
      // Configure to be Read Only
      val statement = conn.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_READ_ONLY)

      // Execute Query
      val rs = statement.executeQuery("select * from sec_user")
      // Iterate Over ResultSet

      while (rs.next) {
        // 返回行号
        // println(rs.getRow)
        val name = rs.getString("name")
        setName = setName :+ name
      }
      return setName
    }
    finally {
      conn.close
    }
  }


}
