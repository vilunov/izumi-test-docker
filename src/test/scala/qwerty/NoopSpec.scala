package qwerty

import izumi.distage.testkit.scalatest.DistageSpecScalatest
import monix.eval.Task

class NoopSpec extends DistageSpecScalatest[Task] {
  "works" in { (_: ExampleDocker.Container) =>
    Task {
      assert(true, "Not works")
    }
  }
}
