package infrastructure.utils.http

import cats.effect.IO
import org.http4s.blaze.client.BlazeClientBuilder

object ClientUtils {

  class HTTPClient {
    val client = BlazeClientBuilder[IO].resource
    def GET: String => IO[String] = {
      { url =>
        client.use { client =>
          client.expect[String](url)
        }
      }

    }
  }
}
