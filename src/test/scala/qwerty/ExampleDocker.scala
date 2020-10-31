package qwerty

import izumi.distage.docker.ContainerDef
import izumi.distage.docker.Docker.DockerPort
import izumi.distage.docker.healthcheck.ContainerHealthCheck

object ExampleDocker extends ContainerDef {
  override def config: Config = Config(
    image = s"example.com/unknown/image:1337",
    ports = Seq.empty,
    env = Map.empty,
  )
}
