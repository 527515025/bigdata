import java.util.HashMap
import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka._
import scala.collection.mutable.ArrayBuffer
import org.apache.spark.{SparkConf}

/**
  * Created by yangyibo on 16/11/28.
  */
object KafkaWordCountDemo {
  private val brokers = "192.168.100.41:9092,192.168.100.42:9092,192.168.100.43:9092"
  // Zookeeper connection properties
  private val props = new HashMap[String, Object]()
  props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
  props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")
  props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
    "org.apache.kafka.common.serialization.StringSerializer")
  private val producer = new KafkaProducer[String, String](this.props)

  def main(args: Array[String]): Unit = {
    run()
  }

  def run(): Unit = {
    val zkQuorum = "192.168.100.48:2181"
    val group = "spark-streaming-test"
    val topics = "abel"
    val numThreads = 1

    val sparkConf = new SparkConf().setAppName("KafkaWordCountDemo")
    //        sparkConf.setMaster("spark://spark1:7077")
    sparkConf.setMaster("local[4]")
    val ssc = new StreamingContext(sparkConf, Seconds(2))
    ssc.checkpoint("checkpoint")


    val topicMap = topics.split(",").map((_, numThreads.toInt)).toMap
    val lines = KafkaUtils.createStream(ssc, zkQuorum, group, topicMap).map(_._2)
//        val words = lines.flatMap(_.split(" "))
//        val wordCounts = words.map(x => (x + "--read", 1L))
//        .reduceByKeyAndWindow(_ + _, _ - _, Seconds(20), Seconds(10), 2)
    val array = ArrayBuffer[String]()
    lines.foreachRDD(rdd => {
      val count = rdd.count().toInt;
      rdd.take(count + 1).take(count).foreach(x => {
        array += x + "--read"
      })
      kafkaProducerSend(array)
      array.clear()
    })
    ssc.start()
    ssc.awaitTermination()
  }

  def kafkaProducerSend(args: ArrayBuffer[String]) {
    if (args != null) {
      val topic = "abel2"
      // Send some messages
      for (arg <- args) {
        println(arg + "----------我已经读取")
        val message = new ProducerRecord[String, String](topic, null, arg)
        producer.send(message)
      }
      Thread.sleep(500)
    }
  }

}