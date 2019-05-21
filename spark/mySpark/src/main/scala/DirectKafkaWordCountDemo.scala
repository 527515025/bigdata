import kafka.serializer.StringDecoder
import org.apache.spark.SparkConf
import org.apache.spark.streaming.kafka.KafkaUtils
import org.apache.spark.streaming.{Seconds, StreamingContext}

/**
  * Created by yangyibo on 16/12/1.
  */
object DirectKafkaWordCountDemo {
  def main(args: Array[String]) {
    val sprakConf = new SparkConf().setAppName("DirectKafkaWordCountDemo")
//    sprakConf.setMaster("local[2]")
    val ssc = new StreamingContext(sprakConf, Seconds(3))

    val brokers = "192.168.100.41:9092,192.168.100.42:9092,192.168.100.43:9092";
    val topics = "abel";
    val topicSet = topics.split(",").toSet
    val kafkaParams = Map[String, String]("metadata.broker.list" -> brokers)
    val messages = KafkaUtils.createDirectStream[String, String, StringDecoder, StringDecoder](ssc, kafkaParams, topicSet)

    val lines = messages.map(_._2)
    val words = lines.flatMap(_.split(" ")).filter(!_.equals("message:"))
    val wordCounts = words.map(x=>(x, 1l)).reduceByKey(_ + _)
    wordCounts.print()
    ssc.start()
    ssc.awaitTermination()
  }

}
