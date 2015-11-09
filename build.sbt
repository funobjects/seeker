name := "seeker"

organization := "org.funobjects"

version := "0.1.0"

scalaVersion := "2.11.7"

resolvers += "Local Maven Repository" at "file://"+Path.userHome.absolutePath+"/.m2/repository"

libraryDependencies ++= {
  object v {
    val akka = "2.4.0"
    val scalatest = "2.2.4"
  }
  List(
    "com.typesafe.akka" %% "akka-actor" % v.akka withSources(),
    "org.cybergarage.cyberlink" % "cyberlink-core" % "2.1.0" withSources(),
    "org.scalatest" %% "scalatest" % "2.2.4" % "test"
  )
}
