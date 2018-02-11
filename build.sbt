import com.typesafe.sbt.packager.universal.ZipHelper

javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

lazy val root = (project in file(".")).
  settings(
    name := "aws-lambda-example",
    version := "1.0",
    scalaVersion := "2.11.4",
    retrieveManaged := true,
    libraryDependencies ++= Seq(
      "com.amazonaws" %  "aws-lambda-java-core"        % "1.1.0",
      "io.circe"      %% "circe-core"                  % "0.7.0",
      "io.circe"      %% "circe-generic"               % "0.7.0",
      "io.circe"      %% "circe-parser"                % "0.7.0"
    )
  )
  .enablePlugins(JavaAppPackaging)


packageBin in Universal :=
  {
    val artifactPath = (packageBin in Universal).value
    val theMappings = (mappings in packageBin in Universal).value
    ZipHelper.zip(theMappings, artifactPath)
    artifactPath
  }