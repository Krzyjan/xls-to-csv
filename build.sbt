name := "XLStoCSV"

organization := "org.krzyjan"

version := "0.1"

scalaVersion := "2.11.2"

libraryDependencies ++= Seq(
	"org.scalatest" % "scalatest_2.11" % "2.2.2",
	"org.apache.poi" % "poi" % "3.10-FINAL",
	"org.apache.poi" % "poi-ooxml" % "3.10-FINAL"
)