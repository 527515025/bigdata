import java.util.HashMap

import org.apache.kafka.clients.producer.{KafkaProducer, ProducerConfig, ProducerRecord}
import org.apache.spark.{SparkConf, SparkContext}

import scala.collection.mutable.ArrayBuffer

/**
  * Created by yangyibo on 16/11/29.
  */
object KafkaProducerDemo {
  def main(args: Array[String]): Unit = {
    val conf = new SparkConf().setAppName("KafkaProducer")
    conf.setMaster("local[2]")
    val sc = new SparkContext(conf)
    val array = ArrayBuffer("one", "tow", "three")
    //      array.foreach(println)
    kafkaProducer(array)
    //      array.clear()
  }

  def kafkaProducer(args: ArrayBuffer[String]) {
    println("我有数据----------------------")
    if (args != null) {
      //      val brokers = "192.168.100.41:9092,192.168.100.42:9092,192.168.100.43:9092"
      val brokers = "192.168.100.71:9092"
      // Zookeeper connection properties
      val props = new HashMap[String, Object]()
      props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, brokers)
      props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer")
      props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
        "org.apache.kafka.common.serialization.StringSerializer")
      val producer = new KafkaProducer[String, String](props)
      val topic = "abcd"
      // Send some messages
      while (true) {
        for (arg <- args) {
          println(arg + "----------我已经读去")
          val message = new ProducerRecord[String, String](topic, null, arg)
          println(producer.send(message).get().offset())

          Thread.sleep(1000)
        }
      }
      //        producer.close()
    }
  }
}
