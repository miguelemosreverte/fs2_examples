package examples._2

object generator {
  def nextInt = Math abs scala.util.Random.nextInt()
  def nextDate: Date = {
    val day = 1
    val month = nextInt % 12
    val year = 2022
    s"$day-$month-$year"
  }
  def nextZooVisitor: Set[Person] = {
    val family1 = Set(
      "Miguel the regular visitor of the Zoo",
      "Anastasia, wife of Miguel"
    )
    val family2 = Set(
      "Franco the regular visitor of the Zoo",
      "Eugenia, wife of Franco",
      "Xara, first children"
    )
    val regulars = Seq(family1, family2)
    regulars(nextInt % regulars.size)
  }

  object nextVisit {
    def apply: ZooVisit =
      ZooVisit(nextDate, nextZooVisitor)

    def show: ZooVisit => String = newVisit =>
      s"${newVisit.persons.mkString(",")} visited on ${newVisit.date}"

    def asJson: ZooVisit => String = newVisit =>
      s"""{
      |"date": "${newVisit.date}", 
      |"persons": [${newVisit.persons
        .map(a => s""" "$a" """)
        .mkString(",")}]
      |}""".stripMargin

    def fromJson: String => Option[ZooVisit] = { str =>
      str.replace("\n", "") match {
        case s"""{"date": "${date}", "persons": [ ${persons} ]}""" =>
          Some(
            ZooVisit(
              date,
              persons.split(",").map(_.replace("\"", "")).toSet
            )
          )
        case _ => None
      }
    }
  }
}
