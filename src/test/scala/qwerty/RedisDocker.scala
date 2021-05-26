package qwerty

import com.github.dockerjava.api.async.ResultCallback
import com.github.dockerjava.api.model.Frame
import izumi.distage.docker.Docker.{DockerPort, DockerReusePolicy}
import izumi.distage.docker.healthcheck.ContainerHealthCheck
import izumi.distage.docker.{ContainerDef, ContainerNetworkDef, DockerClientWrapper}
import izumi.distage.model.definition.ModuleDef
import monix.eval.Task

object RedisClusterDocker extends ContainerDef {
  val portPrimary: DockerPort = DockerPort.DynamicTCP("dynamic_redis_port")

  private val launchScript: String = Seq(
    "mkdir -p /override/",
    "echo 'cluster-enabled yes' > /override/redis.conf",
    "echo 'cluster-announce-ip 127.0.0.1' > /override/redis.conf",
    s"redis-server --port $$${portPrimary.toEnvVariable} /override/redis.conf",
  ).mkString("; ")

  override def config: Config = Config(
    image = "redis:6.2-alpine",
    ports = Vector(portPrimary),
    env = Map.empty,
    healthCheck = ContainerHealthCheck.portCheck,
    entrypoint = Vector("/bin/sh"),
    cmd = Vector("-c", launchScript),
    reuse = DockerReusePolicy.ReuseDisabled,
  )
}

object RedisClusterNetwork extends ContainerNetworkDef {
  override def config: Config = Config(reuse = DockerReusePolicy.ReuseDisabled)
}

case class RedisCluster(containers: Vector[RedisClusterDocker.Container])

object RedisModule extends ModuleDef {
  val clusterSize = 3

  make[RedisClusterNetwork.Network].fromResource {
    RedisClusterNetwork.make[Task]
  }

  for (i <- 0 until clusterSize) {
    many[RedisClusterDocker.Container].addResource {
      RedisClusterDocker.make[Task].connectToNetwork(RedisClusterNetwork).modifyConfig { () => config: RedisClusterDocker.Config =>
        config.copy(
          name = Some(s"redis-$i"),
        )
      }
    }
  }
  make[RedisCluster].from {
    (
      containersSet: Set[RedisClusterDocker.Container],
      clientWrapper: DockerClientWrapper[Task],
    ) =>
      val containers = containersSet.toVector
      val cluster = new RedisCluster(containers)
      val cmdParts = Vector("redis-cli", "--cluster", "create") ++ containers.map { container =>
        val ip = "127.0.0.1"
        val port = container.availablePorts.first(RedisClusterDocker.portPrimary).port
        s"$ip:$port"
      } :+ "--cluster-yes"
      val cmd = clientWrapper.rawClient
        .execCreateCmd(containers.head.id.name)
        .withCmd(cmdParts: _*)
        .withAttachStdout(true)
        .withAttachStderr(true)
        .exec()
      clientWrapper.rawClient.execStartCmd(cmd.getId).exec(new ResultCallback.Adapter[Frame]).awaitCompletion()
      cluster
  }
}

