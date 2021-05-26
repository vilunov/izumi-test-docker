package qwerty

import izumi.distage.docker.modules.DockerSupportModule
import izumi.distage.plugins.PluginDef
import monix.eval.Task

object PluginTest extends PluginDef {
  include(DockerSupportModule[Task])
  include(RedisModule)
}
