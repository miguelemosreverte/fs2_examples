package examples

package object _2 {
  type Date = String
  type Person = String
  type Zoo = Map[Date, Set[Person]]
  val emptyZoo = Map.empty[Date, Set[Person]]

  case class ZooVisit(date: Date, persons: Set[Person])
  case class VisitsPerDay(date: Date, amount: Int)

}
