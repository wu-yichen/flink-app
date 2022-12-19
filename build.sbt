ThisBuild / resolvers ++= Seq(
  "Apache Development Snapshot Repository" at "https://repository.apache.org/content/repositories/snapshots/",
  Resolver.mavenLocal
)

name := "my-flink-app"

ThisBuild / scalaVersion := "2.12.10"

val flinkVersion          = "1.16.0"
val typesafeConfigVersion = "1.4.2"

val flinkDependencies = Seq(
  "org.apache.flink" %% "flink-scala"               % flinkVersion,
  "org.apache.flink" %% "flink-streaming-scala"     % flinkVersion,
  "org.apache.flink" %% "flink-connector-kafka"     % "1.14.4",
  "org.apache.flink" %% "flink-connector-cassandra" % flinkVersion,
  "org.apache.flink"  % "flink-clients"             % "1.16.0",
  "com.typesafe"      % "config"                    % typesafeConfigVersion
)

lazy val root = (project in file(".")).settings(
  libraryDependencies ++= flinkDependencies
)

assembly / mainClass := Some("github.com.yichen.wu.Main")

// make run command include the provided dependencies
Compile / run := Defaults.runTask(Compile / fullClasspath, Compile / run / mainClass, Compile / run / runner).evaluated

// stays inside the sbt console when we press "ctrl-c" while a Flink programme executes with "run" or "runMain"
Compile / run / fork := true
Global / cancelable  := true

// exclude Scala library from assembly
assembly / assemblyOption := (assembly / assemblyOption).value.copy(includeScala = false)

assembly / assemblyMergeStrategy := {
  case PathList("META-INF", _*) => MergeStrategy.discard
  case _                        => MergeStrategy.first
}

assembly / assemblyOutputPath    := file(s"target/${name.value}.jar")
