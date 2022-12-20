package github.com.yichen.wu

import com.typesafe.config.{ Config, ConfigFactory }
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

  val kafkaConfig = KafkaConfig.apply(ConfigFactory.load().getConfig("app.kafka"))

  print(s"===============kafka server: ${kafkaConfig.bootstrapServers}")
  val source = KafkaSource.builder()
    .setBootstrapServers(kafkaConfig.bootstrapServers)
    .setTopics(kafkaConfig.kafkaTopics)
    .setStartingOffsets(OffsetsInitializer.earliest())
    .setValueOnlyDeserializer(new SimpleStringSchema())
    .build()

  val tuples = env.fromSource(source, WatermarkStrategy.noWatermarks(), "KafkaSource")
    .flatMap { (value: String, out: Collector[Tuple1[String]]) =>
      out.collect(Tuple1[String](s"$value you are amazing"))
    }

  val cassandraConfig = CassandraConfig.apply(ConfigFactory.load().getConfig("app.cassandra"))

  CassandraSink.addSink(tuples)
    .setHost(cassandraConfig.cassandraHost, cassandraConfig.cassandraPort.toInt)
    .setQuery(
      s"INSERT INTO ${cassandraConfig.cassandraKeyspaces}.${cassandraConfig.cassandraTables} (payload) values (?);"
    )
    .build()
    .name("flinkTestSink")

  env.execute("flink test")

}

final case class KafkaConfig(bootstrapServers: String, kafkaTopics: String)
object KafkaConfig {
  def apply(config: Config): KafkaConfig =
    KafkaConfig(config.getString("bootstrap-servers"), config.getString("kafka-topics"))
}

final case class CassandraConfig(
    cassandraHost: String,
    cassandraPort: String,
    cassandraKeyspaces: String,
    cassandraTables: String
)
object CassandraConfig {
  def apply(config: Config): CassandraConfig =
    CassandraConfig(
      config.getString("cassandra-host"),
      config.getString("cassandra-port"),
      config.getString("cassandra-keyspaces"),
      config.getString("cassnadra-tables")
    )
}
