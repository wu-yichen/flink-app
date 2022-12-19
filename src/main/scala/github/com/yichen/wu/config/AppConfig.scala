package github.com.yichen.wu.config

sealed trait AppConfig
final case class KafkaConfig(bootstrapServers: String, kafkaTopics: String) extends AppConfig
final case class CassandraConfig(
    cassandraHost: String,
    cassandraPort: String,
    cassandraKeyspaces: String,
    cassandraTables: String
) extends AppConfig
