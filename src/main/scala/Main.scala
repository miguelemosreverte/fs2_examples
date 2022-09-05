import io.prometheus.client.exporter.HTTPServer

object Main extends App {

  import io.prometheus.client.Counter
  val messagesPublished: Counter =
    Counter.build
      .name("messages_published")
      .help("Total messages published to Kafka.")
      .register

  val server: HTTPServer = new HTTPServer.Builder()
    .withPort(9095)
    .build()

  (1 to 100) foreach { _ => messagesPublished.inc() }

  object dataset {
    val messages = Seq(
      "hello!",
      "hi"
    )
  }

  case class UserId(id: String)
  case class ChatId(id: String)
  case class Message(from: UserId, text: String, to: ChatId)

  object Message {
    val greetings = Seq(
      "hello ",
      "nice to meet you ",
      "so long time ",
      "Hello Mr. ",
      "Do you want to buy a boat? "
    )
    val N = greetings.size
    val possibleMessagesN: Int => UserId => String = { index => userId =>
      greetings(index % N) + userId.id
    }

  }

}
