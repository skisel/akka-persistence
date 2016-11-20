import sbt._
import sbt.Keys._
import bintray.BintrayKeys._

object Publish {
	val Common = Seq(
	  publishMavenStyle := true
	)

	val InMemory = Publish.Common ++ Seq(
		pomExtraSetting("akka-persistence-inmemory"),
		homepageSetting("akka-persistence-inmemory"),
		bintrayPackageLabelsSettings("inmemory"),
		bintrayPackageAttributesSettings("akka-persistence-inmemory")
	)

	val Jdbc = Publish.Common ++ Seq(
		pomExtraSetting("akka-persistence-jdbc"),
		homepageSetting("akka-persistence-jdbc"),
		bintrayPackageLabelsSettings("jdbc"),
		bintrayPackageAttributesSettings("akka-persistence-jdbc")
	)

	val JournalWriter = Publish.Common ++ Seq(
		pomExtraSetting("akka-persistence-journal-writer"),
		homepageSetting("akka-persistence-journal-writer"),
		bintrayPackageLabelsSettings("journal", "writer"),
		bintrayPackageAttributesSettings("akka-persistence-journal-writer")
	)

	def pomExtraSetting(name: String) = pomExtra := 
		<scm>
		  <url>https://github.com/dnvriend/${name}</url>
		  <connection>scm:git@github.com:dnvriend/${name}.git</connection>
		  </scm>
		  <developers>
		    <developer>
		      <id>dnvriend</id>
		      <name>Dennis Vriend</name>
		      <url>https://github.com/dnvriend</url>
		    </developer>
		  </developers>

    def homepageSetting(name: String) = 
      homepage := Some(url(s"https://github.com/dnvriend/$name"))

    def bintrayPackageLabelsSettings(labels: String*) = 
	  bintrayPackageLabels := Seq("akka", "persistence") ++ labels

    def bintrayPackageAttributesSettings(name: String) = bintrayPackageAttributes ~=
	  (_ ++ Map(
	    "website_url" -> Seq(bintry.Attr.String(s"https://github.com/dnvriend/$name")),
	    "github_repo" -> Seq(bintry.Attr.String(s"https://github.com/dnvriend/$name.git")),
	    "issue_tracker_url" -> Seq(bintry.Attr.String(s"https://github.com/dnvriend/$name.git/issues/"))
	  )
)
}