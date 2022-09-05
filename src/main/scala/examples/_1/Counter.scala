package examples._1

import cats.effect.{IO, IOApp, Ref}
import infrastructure.fs2.StreamUtils

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object Counter extends IOApp.Simple {

  object service {
    val io: IO[String] = {
      import cats.effect.unsafe.implicits.global
      val counter: Ref[IO, Int] = cats.effect
        .Ref[IO]
        .of(1)
        .unsafeRunSync()
      counter.getAndUpdate(_ + 1) map { done => s"Hello ${done}" }
    }
  }

  def run: IO[Unit] =
    StreamUtils
      .executeIOEvery(
        io = service.io,
        every = 1 second
      )
      .foreach(IO.println)
      .compile
      .drain
}
