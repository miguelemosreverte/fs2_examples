package examples._3

import cats.effect.{ExitCode, IO, IOApp}
import examples._2.VisitingTheZoo.App
import examples._2.generator.nextVisit
import infrastructure.utils.cats.Retry.{restartWhenFailure, retryUntilRight}
import infrastructure.utils.http.ClientUtils.HTTPClient
import infrastructure.utils.http.ServerUtils.serve
import org.http4s.HttpRoutes
import org.http4s.dsl.io._
import org.http4s.server.Router

object ServerAndClient extends IOApp.Simple {

  override def run: IO[Unit] = {
    val server = serve(
      Router
        .of[IO](
          "Zoo" -> HttpRoutes.of[IO] { case GET -> Root / "nextVisitor" =>
            Ok(
              examples._2.generator.nextVisit.asJson(
                examples._2.generator.nextVisit.apply
              )
            )
          }
        )
        .orNotFound
    ).at(8080, "localhost")

    val streamProcessor: IO[Unit] = App.run(
      zooVisitHttpRequest = new HTTPClient()
        .GET("http://localhost:8080/Zoo/nextVisitor")
        .map { e =>
          nextVisit.fromJson(e).get
        }
    )

    IO.race(
      restartWhenFailure(server),
      restartWhenFailure(
        retryUntilRight(streamProcessor)
      )
    ).as(ExitCode.Success)
  }
}
