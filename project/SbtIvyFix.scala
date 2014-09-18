import sbt.Build
import sbt.Project
import java.io.File
import sbt.PathExtra
import sbt.SettingKey
import sbt.Artifact
import sbt.Keys.{artifact, artifactName, artifactPath, packageSrc, packageDoc,crossTarget, projectID, scalaVersion, scalaBinaryVersion, moduleName}
import sbt.ScalaVersion
import sbt.Configurations.{ Compile }
import sbt.Configurations

object SbtIvyFix extends Build with PathExtra {  
  lazy override val projects = Seq(root)
  lazy val root: Project = Project("xlstocsv", new File(".")) settings(
      artifact in (Compile, packageSrc) := Artifact(moduleName.value, "src", "jar", None, List(Configurations.Sources), None, Map()),
      artifact in (Compile, packageDoc) := Artifact(moduleName.value, "doc", "jar", None, List(Configurations.Docs), None, Map()),
      artifactPath in (Compile, packageSrc) <<= myArtifactPathSetting(artifact in packageSrc in Compile),
      artifactPath in (Compile, packageDoc) <<= myArtifactPathSetting(artifact in packageDoc in Compile) 
  )
  
  def myArtifactPathSetting(art: SettingKey[Artifact]) = (crossTarget, projectID, art, scalaVersion in artifactName, scalaBinaryVersion in artifactName, artifactName) {
    (t, module, a, sv, sbv, toString) =>
      {
        t / a.`type` / toString(ScalaVersion(sv, sbv), module, a)
      }
  }
}


