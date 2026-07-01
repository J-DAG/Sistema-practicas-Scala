ThisBuild / scalaVersion := "2.13.14"

lazy val root = (project in file("."))
  .settings(
    name := "SistemaPracticasPreprofesionalesScala",
    Compile / unmanagedSourceDirectories += baseDirectory.value / "src",
    Compile / unmanagedResourceDirectories += baseDirectory.value / "src" / "main" / "resources",
    libraryDependencies ++= Seq(
      "org.postgresql" % "postgresql" % "42.7.4",
      "org.mindrot" % "jbcrypt" % "0.4"
    )
  )
