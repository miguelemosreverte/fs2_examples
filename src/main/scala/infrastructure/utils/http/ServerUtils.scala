package infrastructure.utils.http

import cats.effect.Async
import org.http4s.HttpApp
import org.http4s.blaze.server.BlazeServerBuilder

object ServerUtils {

  case class At[F[_]: Async](api: HttpApp[F]) {
    def at(port: Int, host: String) =
      BlazeServerBuilder[F]
        .bindHttp(port, host)
        .withHttpApp(api)
        .serve
        .compile
        .drain
  }
  def serve[F[_]: Async](api: HttpApp[F]): At[F] = At(api)

}
