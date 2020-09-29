package qwerty

import distage.ModuleDef
import izumi.distage.docker.Docker
import izumi.distage.docker.modules.DockerSupportModule
import izumi.distage.plugins.PluginDef
import monix.eval.Task
import monix.execution.Scheduler

object PluginTest extends PluginDef {
  make[Scheduler].from(Scheduler.global)

  include(new DockerSupportModule[Task].overridenBy(new ModuleDef {
    make[Docker.ClientConfig].from {
      Docker.ClientConfig(
        readTimeoutMs = 60000,
        connectTimeoutMs = 500,
      )
    }
  }))

  include(ModuleMonix)

  make[ExampleDocker.Container].fromResource(ExampleDocker.make[Task])
}
