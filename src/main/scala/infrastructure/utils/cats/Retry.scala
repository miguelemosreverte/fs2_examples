package infrastructure.utils.cats

import cats.effect.IO

object Retry {
  def retryUntilRight[A](io: IO[A]): IO[A] = {
    io.attempt.flatMap {
      case Right(b) => IO.pure(b)
      case Left(error) =>
        println(error)
        retryUntilRight(io)
    }
  }
}
