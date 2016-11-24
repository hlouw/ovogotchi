scalaVersion in ThisBuild := "2.12.0"

val server = project.settings(
  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.0.0"
  )
)

