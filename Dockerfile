FROM flink:scala_2.12-java11
WORKDIR /opt/flink/bin
COPY ./target/my-flink-app.jar /opt/my-flink-app.jar