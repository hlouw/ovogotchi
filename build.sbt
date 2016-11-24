scalaVersion in ThisBuild := "2.11.8"

val server = project.settings(

  libraryDependencies ++= Seq(
    "com.typesafe.akka" %% "akka-http" % "10.0.0",
    "io.circe" %% "circe-generic-extras" % "0.6.1",
    "com.github.gilbertw1" %% "slack-scala-client" % "0.1.8"
  )
)
