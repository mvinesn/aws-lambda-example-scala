javacOptions ++= Seq("-source", "1.8", "-target", "1.8", "-Xlint")

scalacOptions := Seq("-unchecked", "-feature", "-deprecation", "-encoding", "utf8")

lazy val root = (project in file(".")).
  settings(
    name := "lambda-demo",
    version := "1.0",
    scalaVersion := "2.11.4",
    retrieveManaged := true,
    libraryDependencies += "com.amazonaws" % "aws-lambda-java-core"  % "1.1.0"
  )

assemblyMergeStrategy :=
  {
    case PathList("META-INF", xs @ _*) => MergeStrategy.discard
    case x => MergeStrategy.first
  }
