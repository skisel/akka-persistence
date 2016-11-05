import sbt._
import sbt.Keys._

import de.heikoseeberger.sbtheader._
import de.heikoseeberger.sbtheader.HeaderKey._
import de.heikoseeberger.sbtheader.license.Apache2_0
import com.typesafe.sbt.SbtScalariform
import com.typesafe.sbt.SbtScalariform.ScalariformKeys

object Common extends AutoPlugin {
  
  override def trigger = allRequirements
  override def requires = plugins.JvmPlugin && HeaderPlugin

  object autoImport {
  }

  import autoImport._

  override lazy val projectSettings = SbtScalariform.scalariformSettings ++ Dependencies.Common ++ Seq(
        organization := "com.github.dnvriend",
        organizationName := "Dennis Vriend",
        description := "Plugins and tooling for akka-persistence",
        startYear := Some(2016),

        licenses := Seq(("Apache License, Version 2.0", url("http://www.apache.org/licenses/LICENSE-2.0"))),

        scalaVersion := "2.11.8",
        crossScalaVersions := Dependencies.ScalaVersions,
        crossVersion := CrossVersion.binary,

        fork in Test := true,

        parallelExecution in Test := false,

        scalacOptions ++= Seq(
          "-encoding",
          "UTF-8",
          "-deprecation",
          "-feature",
          "-unchecked",
          "-Xlog-reflective-calls",
          "-language:higherKinds",
          "-language:implicitConversions",
          "-target:jvm-1.8"
        ),

        javacOptions ++= Seq(
        "-Xlint:unchecked"
        ),

        // show full stack traces and test case durations
        testOptions in Test += Tests.Argument("-oDF"),

        headers := headers.value ++ Map(
          "scala" -> Apache2_0("2016", "Dennis Vriend"),
          "conf" -> Apache2_0("2016", "Dennis Vriend", "#")
        ),

        resolvers += Resolver.typesafeRepo("releases"),
        resolvers += Resolver.jcenterRepo,

        ScalariformKeys.preferences in Compile  := formattingPreferences,
        ScalariformKeys.preferences in Test     := formattingPreferences
  )

  def formattingPreferences = {
    import scalariform.formatter.preferences._
    FormattingPreferences()
        .setPreference(AlignSingleLineCaseStatements, true)
        .setPreference(AlignSingleLineCaseStatements.MaxArrowIndent, 100)
        .setPreference(DoubleIndentClassDeclaration, true)
  }
}