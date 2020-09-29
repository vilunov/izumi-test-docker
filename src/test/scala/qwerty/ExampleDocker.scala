package qwerty

import izumi.distage.docker.ContainerDef
import izumi.distage.docker.Docker.DockerPort
import izumi.distage.docker.healthcheck.ContainerHealthCheck

object ExampleDocker extends ContainerDef {
  val port: DockerPort = DockerPort.TCP(8080)

  override def config: Config = Config(
    image = s"example.com/unknown/image:1337",
    ports = Seq(port),
    env = Map.empty,
    healthCheck = ContainerHealthCheck.httpGetCheck(port),
  )
}