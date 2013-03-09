import AssemblyKeys._ // put this at the top of the file

assemblySettings

name := "bs-commlog-digest"

version := "0.1"

scalaVersion := "2.10.0"

mainClass in assembly := Some("nu.ganslandt.commlog.tools.Main")

libraryDependencies += "org.scalatest" % "scalatest_2.10" % "1.9.1" % "test"

libraryDependencies += "com.github.nscala-time" %% "nscala-time" % "0.2.0"
