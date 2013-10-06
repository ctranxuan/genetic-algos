import sbt._
import Keys._

object BuildSettings {
  val VERSION = "1.0.0-SNAPSHOT"

  lazy val basicSettings = seq(
    version               := "0.0.1-SNAPSHOT",
    organization          := "org.workspace13.genetics",
    startYear             := Some(2013),
    licenses              := Seq("Apache 2" -> new URL("http://www.apache.org/licenses/LICENSE-2.0.txt")),
    scalaVersion          := "2.10.2",
    resolvers             ++= Dependencies.resolutionRepos
  )

  lazy val geneticsModuleSettings = basicSettings
}
