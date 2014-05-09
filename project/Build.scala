import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "me.elrod",
    version := "0.2-SNAPSHOT",
    publishTo := Some(Resolver.file("file", new File("releases"))),
    resolvers ++= Seq(
      "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/"
    ),
    scalaVersion := "2.10.4",
    scalacOptions ++= Seq(
      "-deprecation",
      "-encoding", "UTF-8",
      "-feature",
      "-language:existentials",
      "-language:higherKinds",
      "-language:implicitConversions",
      "-unchecked",
      "-Xfatal-warnings",
      "-Xlint",
      "-Yno-adapted-args",
      "-Ywarn-dead-code",
      "-Ywarn-numeric-widen",
      "-Ywarn-value-discard"
    ),
    addCompilerPlugin("org.brianmckenna" %% "wartremover" % "0.8")
  )
}

//scalacOptions in (Compile, compile) += "-P:wartremover:traverser:org.brianmckenna.wartremover.warts.Unsafe"

object Dependencies {
  val scalaz = "org.scalaz" %% "scalaz-core" % "7.0.6"
  val scalazEffect = "org.scalaz" %% "scalaz-effect" % "7.0.6"
  val monocle = "com.github.julien-truffaut"  %%  "monocle-core"  % "0.3"
}

object PhBuild extends Build {
  import BuildSettings._
  import Dependencies._

  lazy val root: Project = Project(
    "ph",
    file("."),
    settings = buildSettings ++ Seq(
      publishArtifact := false,
      run <<= run in Compile in core)
  ) aggregate(core, examples)

  lazy val core: Project = Project(
    "ph-core",
    file("core"),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= Seq(scalaz, scalazEffect, monocle)
    )
  )

  lazy val examples: Project = Project(
    "ph-examples",
    file("examples"),
    settings = buildSettings ++ Seq(
      publishArtifact := false,
      libraryDependencies ++= Seq(scalaz, scalazEffect)
    )
  ) dependsOn(core % "test->test;compile->compile")
}
