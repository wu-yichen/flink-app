# Flink demo with Kafka source and Cassandra sink

This is a demo of Flink Streaming Processing Application that consumes events from Kafka and 
persists the transformed events into a Cassandra data store. 

The Kafka, Cassandra and Flink clusters are created and fully run in docker.

## Contents

* [Dependencies](#dependencies)
* [Building](#building)
* [Interacting with Kafka](#interacting with Kafka)
* [Interacting with Cassandra](#interacting with Cassandra)
* [Accessing Flink's Web UI](#accessing-flinks-web-ui)

---
## Dependencies

The application is based on the Flink 1.16-scala_2.12-java11 docker image which has the following dependencies:

- Java 11
- Scala >= 2.12

---
## Building

The current build system used is SBT (1.5.8).

To install dependencies locally, compile and build a jar with dependencies you can use the following command:

`make build-jar`

To build a fat jar and run the application locally, run:

`make full-run`

To run the application without building jar, run:

`make run`

---
## Interacting with Kafka
 
To connect to the Kafka container, you can use the following command:

`docker-compose exec -it broker /bin/bash`

To publish messages to kafka:

`kafka-console-producer --bootstrap-server broker:29092 --topic flink-input`

---
## Interacting with Cassandra

The initial keyspace is `
cassandrasink` and table is `messages`

To connect to the Cassandra container, you can use the following command:

`docker-compose exec -it cassandra /bin/bash`

you can use cqlsh to check the records published from the Flink job

---
## Accessing Flink's Web UI

To access the Flink's Web UI, you can open your local browser and open `localhost:8081`.
The Flink Web Dashboard will display.