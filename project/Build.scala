import sbt._
import Keys._

object GeneticsBuild extends Build {
  import BuildSettings._
  import Dependencies._

  val resolutionRepos = Seq(
    "Twitter Maven Repo" at "http://maven.twttr.com/",
    "Sonatype snapshots" at "http://oss.sonatype.org/content/repositories/snapshots/"
  )

  lazy val parent = Project(id = "genetic-algos",
    base = file("."))
    .aggregate (geneticsBasic1)
    .settings(basicSettings: _*)

  lazy val geneticsBasic1 = Project(id = "genetic-basic1", base = file("genetic-basic1"))
    .settings(geneticsModuleSettings: _*)
    .settings(libraryDependencies ++=
    compile(twitterUtil) ++
      test(scalaTest))
}
