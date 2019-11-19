val catsVersion = "2.0.0"

val scalatest = "org.scalatest" %% "scalatest" % "3.0.8" % "test"
val nscalaTime = "com.github.nscala-time" %% "nscala-time" % "2.22.0"
val cats = "org.typelevel" %% "cats-core" % catsVersion
val `cats-effects`  = "org.typelevel" %% "cats-effect" % catsVersion

lazy val phoneCompany = (project in file(".")).settings(
  Seq(
    name := "disco-test-phone-company",
    version := "1.0",
    scalaVersion := "2.13.1",
    libraryDependencies := Seq(
      nscalaTime,
      cats,
      `cats-effects`,
      scalatest
    )
  )
)

//https://stackoverflow.com/questions/55493096/listing-files-from-resource-directory-in-sbt-1-2-8
run := Defaults.runTask(fullClasspath in Runtime, mainClass in run in Compile, runner in run).evaluated
