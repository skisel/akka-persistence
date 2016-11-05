lazy val akkaPersistence = project
  .in(file("."))
  .aggregate(inmemory, jdbc, journalWriter)
  .settings(
    publishArtifact := false
  )

lazy val inmemory = project
  .in(file("inmemory"))
  .enablePlugins(AutomateHeaderPlugin, ReleasePlugin)
  .settings(
    version := "1.3.15-SNAPSHOT",
    name := "akka-persistence-inmemory",
    Dependencies.InMemory,
    Publish.InMemory
  )

lazy val jdbc = project
  .in(file("jdbc"))
  .enablePlugins(AutomateHeaderPlugin, ReleasePlugin)
  .settings(
    name := "akka-persistence-jdbc",
    crossScalaVersions := Dependencies.ScalaVersions.take(1),
    Dependencies.Jdbc,
    Publish.Jdbc
  )

lazy val journalWriter = project
  .in(file("journal-writer"))
  .enablePlugins(AutomateHeaderPlugin, ReleasePlugin)
  .settings(
    name := "akka-persistence-journal-writer",
    Dependencies.JournalWriter
  ).dependsOn(inmemory % "test -> compile")

lazy val streamsExtension = project
  .in(file("stream-extensions"))
  .enablePlugins(AutomateHeaderPlugin, ReleasePlugin)
  .settings(
    name := "akka-streams-extensions",
    Dependencies.StreamsExtensions
  ).dependsOn(inmemory % "test -> compile")

lazy val resumableQuery = project
  .in(file("resumable-query"))
  .enablePlugins(AutomateHeaderPlugin, ReleasePlugin)
  .settings(
    name := "akka-persistence-resumable-query",
    Dependencies.ResumableQuery
  ).dependsOn(streamsExtension, inmemory % "test -> compile", journalWriter % "test -> compile")

lazy val httpClient = project
  .in(file("http-client"))
  .enablePlugins(AutomateHeaderPlugin, ReleasePlugin)
  .settings(
    name := "akka-http-client",    
    crossScalaVersions := Dependencies.ScalaVersions.take(1),
    Dependencies.HttpClient
  )

lazy val docs = project
  .in(file("paradox"))
  .enablePlugins(ParadoxPlugin)
  .settings(
    version := "",
    name := "akka-persistence",
    paradoxTheme := Some(builtinParadoxTheme("generic")),
    paradoxProperties ++= Map(
      "version" -> version.value,
      "scala.binaryVersion" -> scalaBinaryVersion.value,
      "snippet.base_dir" -> s"${(baseDirectory in akkaPersistence).value}",
      "jdbcVersion" -> (version in jdbc).value,
      "inmemoryVersion" -> (version in inmemory).value,
      "journalWriterVersion" -> (version in journalWriter).value,
      "github.base_url" -> "https://github.com/dnvriend/akka-persistence",
      "extref.akka-docs.base_url" -> s"http://doc.akka.io/docs/akka/latest/%s.html",            
      "scaladoc.akka.base_url" -> s"http://doc.akka.io/api/akka/latest"
    ),
    publishArtifact := false
  )