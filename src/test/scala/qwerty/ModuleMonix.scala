package qwerty

import cats.{Applicative, Functor, Monad, MonadError}
import cats.effect.{Async, Bracket, Sync}
import distage.ModuleDef
import izumi.distage.model.effect.{DIApplicative, DIEffect, DIEffectAsync, DIEffectRunner}
import monix.eval.Task
import monix.execution.Scheduler

object ModuleMonix extends ModuleDef {
  make[DIEffectRunner[Task]].from { implicit scheduler: Scheduler =>
    new DIEffectRunner[Task] {
      override def run[A](f: => Task[A]): A = f.runSyncUnsafe()
    }
  }
  addImplicit[DIEffect[Task]].aliased[DIApplicative[Task]]
  addImplicit[DIEffectAsync[Task]]

  addImplicit[Async[Task]]
    .aliased[Sync[Task]]
    .aliased[Bracket[Task, Throwable]]
    .aliased[MonadError[Task, Throwable]]
    .aliased[Monad[Task]]
    .aliased[Applicative[Task]]
    .aliased[Functor[Task]]
}
