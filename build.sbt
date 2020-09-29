name := "izumi-test-docker"

version := "0.1"

scalaVersion := "2.13.3"

libraryDependencies ++= Vector(
  "io.7mind.izumi" %% "distage-framework-docker",
  "io.7mind.izumi" %% "distage-testkit-scalatest",
).map(_ % "0.10.19") ++ Vector(
  "ru.tinkoff" %% "tofu-env" % "0.8.0",
)
