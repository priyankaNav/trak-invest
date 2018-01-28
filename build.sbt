name := """trackInvest"""

version := "1.0-SNAPSHOT"

lazy val root = (project in file(".")).enablePlugins(PlayJava)

scalaVersion := "2.11.7"

libraryDependencies ++= Seq(
  javaJdbc,
  cache,
  javaWs,
  "org.mongodb.morphia"%"morphia"%"1.3.2",
  "org.mindrot" % "jbcrypt" % "0.4"
)
