package infrastructure.utils.fs2

import cats.effect.Async
import fs2.Stream

import scala.concurrent.duration.{DurationInt, FiniteDuration}
import scala.language.postfixOps

object StreamUtils {

  def executeIOEvery[F[_]: Async, A](
      io: => F[A],
      every: FiniteDuration = 1 second
  ): Stream[F, A] =
    Stream
      .awakeEvery(every)
      .evalMap(_ => io)

}
