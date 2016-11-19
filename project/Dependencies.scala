import sbt._
import sbt.Keys._

object Dependencies {
    val ScalaVersions = Seq("2.11.8", "2.12.0")
    val ScalaXmlVersion = "1.0.5"
    val AkkaVersion = "2.4.13"
    val HttpVersion = "3.0.0-RC1"
    val ScalazVersion = "7.2.7"
    val CassandraVersion = "3.1.2"
    val SlickVersion = "3.1.1"
    val HikariCPVersion = "2.5.1"
    val CommonsIoVersion = "2.5"

    val Common = Seq(
      libraryDependencies ++= Seq(
       "com.typesafe.akka" %% "akka-actor" % AkkaVersion,
       "com.typesafe.akka" %% "akka-persistence" % AkkaVersion,
       "com.typesafe.akka" %% "akka-persistence-query-experimental" % AkkaVersion,
       "com.typesafe.akka" %% "akka-stream" % AkkaVersion,
       "com.typesafe.akka" %% "akka-slf4j" % AkkaVersion % Test,
       "ch.qos.logback" % "logback-classic" % "1.1.7" % Test,
       "com.typesafe.akka" %% "akka-persistence-tck" % AkkaVersion % Test,
       "com.typesafe.akka" %% "akka-stream-testkit" % AkkaVersion % Test,
       "com.typesafe.akka" %% "akka-testkit" % AkkaVersion % Test,
       "org.scalatest" %% "scalatest" % "3.0.0" % Test 
      )
    )

    val Jdbc = Seq(
      libraryDependencies ++= Seq(
        "com.typesafe.slick" %% "slick" % SlickVersion,
        "com.typesafe.slick" %% "slick-extensions" % "3.1.0" % Test,
        "com.typesafe.slick" %% "slick-hikaricp" % SlickVersion exclude("com.zaxxer", "HikariCP-java6"),
        "com.zaxxer" % "HikariCP" % HikariCPVersion,
        "org.postgresql" % "postgresql" % "9.4.1212" % Test,
        "com.h2database" % "h2" % "1.4.193" % Test,
        "mysql" % "mysql-connector-java" % "6.0.5" % Test
      )
    )

    val InMemory = Seq(
      libraryDependencies ++= Seq(
        "org.scalaz" %% "scalaz-core" % ScalazVersion,
        "com.datastax.cassandra" % "cassandra-driver-core" % CassandraVersion
      )
    )

    val JournalWriter = Seq(
      libraryDependencies ++= Seq(
        "commons-io" % "commons-io" % CommonsIoVersion % Test,
        "org.iq80.leveldb" % "leveldb" % "0.9" % Test,
        "org.fusesource.leveldbjni" % "leveldbjni-all" % "1.8" % Test
      )
    )

    val StreamsExtensions = Seq(
      libraryDependencies ++= Seq(
        "commons-io" % "commons-io" % CommonsIoVersion,
        "org.scala-lang.modules" %% "scala-xml" % ScalaXmlVersion
      )
    )

    val ResumableQuery = Seq(
      libraryDependencies ++= Seq(
        "org.scalaz" %% "scalaz-core" % ScalazVersion        
      )
    )

    val HttpClient = Seq(
      libraryDependencies ++= Seq(
        "com.typesafe.akka" %% "akka-http-core" % HttpVersion,
        "com.typesafe.akka" %% "akka-http-spray-json" % HttpVersion,
        "com.typesafe.akka" %% "akka-http-xml" % HttpVersion,
        "com.hunorkovacs" %% "koauth" % "1.1.0" exclude("com.typesafe.akka", "akka-actor_2.11") exclude("org.specs2", "specs2_2.11"),
        "com.typesafe.akka" %% "akka-http-testkit" % HttpVersion % Test    
      )
    )

  val HttpMarshaller = Seq(
    libraryDependencies ++= Seq(
      "com.typesafe.akka" %% "akka-http-core" % HttpVersion,
      "com.typesafe.akka" %% "akka-http-spray-json" % HttpVersion,
      "com.typesafe.akka" %% "akka-http-xml" % HttpVersion,
      "com.hunorkovacs" %% "koauth" % "1.1.0" exclude("com.typesafe.akka", "akka-actor_2.11") exclude("org.specs2", "specs2_2.11"),
      "org.scalaz" %% "scalaz-core" % ScalazVersion,
      "com.typesafe.akka" %% "akka-http-testkit" % HttpVersion % Test
    )
  )
}