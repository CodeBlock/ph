import sbt._
import Keys._

object BuildSettings {
  val buildSettings = Defaults.defaultSettings ++ Seq(
    organization := "me.elrod",
    version := "0.2-SNAPSHOT",
    publishTo := Some(Resolver.file("file", new File("releases"))),
    resolvers ++= Seq(
      "Sonatype OSS Releases" at "http://oss.sonatype.org/content/repositories/releases/",
      "Ricky Elrod - Fedorapeople" at "http://codeblock.fedorapeople.org/maven/"
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
  val scalazVersion = "7.0.6"
  val monocleVersion = "0.3.0"

  val scalaz            = "org.scalaz" %% "scalaz-core" % scalazVersion
  val scalazEffect      = "org.scalaz" %% "scalaz-effect" % scalazVersion
  val monocle           = "com.github.julien-truffaut" %% "monocle-core" % monocleVersion
  val monocleGeneric    = "com.github.julien-truffaut" %% "monocle-generic" % monocleVersion
  val bytestring        = "org.purefn" %% "bytestring" % "1.0-SNAPSHOT"

  // Tests
  val scalaCheck        = "org.scalacheck" %% "scalacheck" % "1.11.4"
  val scalaCheckBinding = "org.scalaz" %% "scalaz-scalacheck-binding" % scalazVersion
  val specs2            = "org.specs2" %% "specs2" % "2.3.11"
  val scalazSpecs2      = "org.typelevel" %% "scalaz-specs2" % "0.2"
  val monocleLaw        = "com.github.julien-truffaut" %% "monocle-law" % monocleVersion
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
  ) aggregate(core, examples, test)

  lazy val core: Project = Project(
    "ph-core",
    file("core"),
    settings = buildSettings ++ Seq(
      libraryDependencies ++= Seq(
        bytestring,
        monocle,
        monocleGeneric,
        scalaz,
        scalazEffect)
    )
  )

  lazy val examples: Project = Project(
    "ph-examples",
    file("examples"),
    settings = buildSettings ++ Seq(
      publishArtifact := false,
      libraryDependencies ++= Seq(
        bytestring,
        monocle,
        monocleGeneric,
        scalaz,
        scalazEffect)
    )
  ) dependsOn(core % "test->test;compile->compile")


  lazy val test: Project = Project(
    "ph-test",
    file("tests"),
    settings = buildSettings ++ Seq(
      publishArtifact := false,
      libraryDependencies ++= Seq(
        bytestring,
        monocle,
        scalaCheck,
        scalaCheckBinding,
        scalaz,
        scalazEffect,
        scalazSpecs2,
        specs2)
    )
  ) dependsOn(core)
}
