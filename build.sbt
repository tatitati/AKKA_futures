name := "actors_hierarchy"

version := "0.1"

scalaVersion := "2.12.7"

resolvers += "Artima Maven Repository" at "http://repo.artima.com/releases"

libraryDependencies += "com.typesafe.akka" %% "akka-actor" % "2.5.17"

libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.4" % Test
libraryDependencies += "com.typesafe.akka" %% "akka-testkit" % "2.5.22" % Test