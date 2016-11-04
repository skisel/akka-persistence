import sbt._
import sbt.Keys._

import com.lightbend.paradox.sbt.ParadoxPlugin
import com.lightbend.paradox.sbt.ParadoxPlugin.autoImport._

object CopyApiDocs extends AutoPlugin {
  override def trigger = allRequirements
  override def requires = ParadoxPlugin

  // When an auto plugin provides a stable field such as val or object named autoImport,
  // the contents of the field are wildcard imported in set, eval, and .sbt files.
  object autoImport {
    val copyDocs = taskKey[File]("Generate paradox site and copy the files to the /docs directory")
  }

  import autoImport._
   val copyApiDocsSettings: Seq[Setting[_]] = ParadoxPlugin.paradoxSettings ++ Seq(
     copyDocs := {
        val siteDir: File = paradox.value
        IO.copyDirectory(siteDir, baseDirectory.value / ".." / "docs", overwrite = true)
        siteDir
    }
  )

  override def projectSettings: Seq[_root_.sbt.Def.Setting[_]] =
    inConfig(Compile)(copyApiDocsSettings)
}