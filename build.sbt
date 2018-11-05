name := "actors_hierarchy"

version := "0.1"

scalaVersion := "2.12.7"


resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"
libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % "test"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.17"