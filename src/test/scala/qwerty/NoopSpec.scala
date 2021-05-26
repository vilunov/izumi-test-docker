package qwerty

import izumi.distage.testkit.scalatest.DistageSpecScalatest
import monix.eval.Task

class NoopSpec extends DistageSpecScalatest[Task] {
  "works" in { (cluster: RedisCluster) =>
    Task {
      println(cluster.containers.length)
      assert(true, "Not works")
    }
  }
}
