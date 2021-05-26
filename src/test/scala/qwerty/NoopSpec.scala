package qwerty

import izumi.distage.testkit.scalatest.Spec1
import monix.eval.Task

class NoopSpec extends Spec1[Task] {
  "works" in { (cluster: RedisCluster) =>
    Task {
      println(cluster.containers.length)
      assert(true, "Not works")
    }
  }
}
