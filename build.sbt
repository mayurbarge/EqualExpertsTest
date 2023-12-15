import sbt.Keys.libraryDependencies

ThisBuild / scalaVersion := "2.13.8"
ThisBuild / version := "1.0-SNAPSHOT"
ThisBuild / name := """EqualExperts"""

val AkkaVersion = "2.6.18"
val playVersion = "2.8.13"

ThisBuild / scalacOptions ++= Seq(
  "-deprecation",
  "-feature",
  "-language:_",
  "-unchecked",
  //"-Wunused:_",
  "-Wvalue-discard",
  "-Xfatal-warnings",
  "-Ymacro-annotations"
)

lazy val `EqualExperts`
= project
  .in(file("."))
  .enablePlugins(PlayScala)
  .settings(commonSettings)
  .settings(
    libraryDependencies ++= Seq(
      guice,
      "org.typelevel" %% "cats-core" % "2.7.0",
      "com.typesafe.play" %% "play-json" % "2.8.2",
      "org.mockito" % "mockito-core" % "4.3.1",
      "org.scalatestplus.play" %% "scalatestplus-play" % "5.0.0" % Test)
  )
  .settings(
    resolvers += Resolver.sonatypeRepo("snapshots"),
    libraryDependencies ++= Seq(
      "com.github.fd4s" %% "fs2-kafka" % "3.0.0-M4",
      "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
      "io.github.embeddedkafka" % "embedded-kafka_2.13" % "3.1.0" % Test
    )
  )

lazy val commonSettings = Seq(
  addCompilerPlugin("org.typelevel" % "kind-projector" % "0.13.2" cross CrossVersion.full),
  Compile / console / scalacOptions --= Seq(
    "-Wunused:_",
    "-Xfatal-warnings"
  ),
  Test / console / scalacOptions :=
    (Compile / console / scalacOptions).value

)
