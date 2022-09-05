package examples._2

import cats.effect._
import infrastructure.utils.fs2.StreamUtils

import scala.concurrent.duration.DurationInt
import scala.language.postfixOps

object VisitingTheZoo extends IOApp.Simple {

  object service {
    val httpRequest: IO[ZooVisit] = IO(generator.nextVisit.apply)

    def aggregate: ZooVisit => IO[Zoo] = {
      import cats.effect.unsafe.implicits.global
      val visitorsRecords: Ref[IO, Zoo] = cats.effect
        .Ref[IO]
        .of(emptyZoo)
        .unsafeRunSync()

      { zooVisit =>
        visitorsRecords.getAndUpdate { a =>
          val (date, visitors) = {
            val e = zooVisit
            e.date -> e.persons
          }
          a.updatedWith(date) {
            case Some(otherVisitors) => Some(otherVisitors ++ visitors)
            case None                => Some(visitors)
          }
        }
      }
    }

    object personsPerDate {
      val calculate: Zoo => Seq[VisitsPerDay] = { zoo =>
        zoo.map { case (date, value) => VisitsPerDay(date, value.size) }.toSeq
      }
      def show: Seq[VisitsPerDay] => String = visitsPerDay =>
        s"""
          |---------Report
          |Visits per day:
          |${visitsPerDay
          .map { case VisitsPerDay(date, i) => s"$date: $i" }
          .sorted
          .mkString("\n")}
          |""".stripMargin

    }

  }

  object App {
    def run(zooVisitHttpRequest: => IO[ZooVisit]): IO[Unit] =
      StreamUtils
        .executeIOEvery(
          io = zooVisitHttpRequest,
          every = 1 second
        )
        .evalTap(generator.nextVisit.show andThen IO.println)
        .evalMap { service.aggregate }
        .map(service.personsPerDate.calculate)
        .evalTap(service.personsPerDate.show andThen IO.println)
        .compile
        .drain
  }

  override def run: IO[Unit] =
    App.run(
      zooVisitHttpRequest = service.httpRequest
    )
}
