package ovogotchi.input

sealed trait Effect
object Effect {
  case object BecomeAngry extends Effect
}

case class Link(label: String, url: String)

// everything is optional, just fill in whatever info you have
case class Person(
                 name: Option[String] = None,
                 githubUsername: Option[String] = None,
                 emailAddress: Option[String] = None
                 )

case class InputEvent(
                     source: String,
                     effects: Seq[Effect],
                     description: String,
                     links: Seq[Link],
                     relevantPeople: Seq[Person]
                     )
