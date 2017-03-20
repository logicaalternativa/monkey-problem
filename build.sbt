name := "monkey-problem"

version := "1.0"

scalaVersion := "2.12.1"


libraryDependencies ++= Seq(
  "com.typesafe.akka" %% "akka-actor" % "2.4.17",
  "ch.qos.logback" % "logback-classic" % "1.1.3",
  "com.typesafe.akka" %% "akka-slf4j" % "2.4.17",
  "org.json4s" %% "json4s-jackson" % "3.5.0",
  "org.scalactic" %% "scalactic" % "3.0.1",
  "org.scalaz" %% "scalaz-core" % "7.2.7",
  "org.scalaz" %% "scalaz-scalacheck-binding" % "7.2.7",
  "org.scalatest" %% "scalatest" % "3.0.1" % "test"
)


