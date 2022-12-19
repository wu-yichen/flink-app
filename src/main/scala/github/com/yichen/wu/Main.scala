package github.com.yichen.wu

import org.apache.flink.api.common.eventtime.WatermarkStrategy
import org.apache.flink.api.common.serialization.SimpleStringSchema
import org.apache.flink.api.scala.createTypeInformation
import org.apache.flink.connector.kafka.source.KafkaSource
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer
import org.apache.flink.streaming.api.scala.StreamExecutionEnvironment
import org.apache.flink.streaming.connectors.cassandra.CassandraSink
import org.apache.flink.util.Collector

object Main extends App {

  val env = StreamExecutionEnvironment.getExecutionEnvironment

  val source = KafkaSource.builder()
    .setBootstrapServers("localhost:9092")
    .setTopics("flink-input")
    .setStartingOffsets(OffsetsInitializer.earliest())
    .setValueOnlyDeserializer(new SimpleStringSchema())
    .build()

  val tuples = env.fromSource(source, WatermarkStrategy.noWatermarks(), "KafkaSource")
    .flatMap { (value: String, out: Collector[Tuple1[String]]) =>
      out.collect(Tuple1[String](s"$value you are amazing"))
    }

  CassandraSink.addSink(tuples)
    .setHost("localhost", 9042)
    .setQuery("INSERT INTO cassandrasink.messages (payload) values (?);")
    .build()
    .name("flinkTestSink")

  env.execute("flink test")
}
