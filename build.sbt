lazy val root = project
  .in(file("."))
  .aggregate(inmemory, jdbc, journalWriter)
  .settings(
    publishArtifact := false
  )

lazy val inmemory = project
  .in(file("inmemory"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    version := "1.3.15-SNAPSHOT",
    name := "akka-persistence-inmemory",
    Dependencies.InMemory,
    Publish.InMemory
  )

lazy val jdbc = project
  .in(file("jdbc"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    version := "2.6.9-SNAPSHOT",
    name := "akka-persistence-jdbc",
    crossScalaVersions := Dependencies.ScalaVersions.take(1),
    Dependencies.Jdbc,
    Publish.Jdbc
  )

lazy val journalWriter = project
  .in(file("journal-writer"))
  .enablePlugins(AutomateHeaderPlugin)
  .settings(
    version := "0.0.3-SNAPSHOT",
    name := "akka-persistence-journal-writer",
    Dependencies.JournalWriter
  ).dependsOn(inmemory)