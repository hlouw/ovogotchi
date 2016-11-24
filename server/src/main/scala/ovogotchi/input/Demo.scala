package ovogotchi.input

import ovogotchi.emotion.EmotionEngine

object Demo {

  def buildFailed(): Unit = {
    EmotionEngine.handleEvent(InputEvent(
      source = "CI tool",
      effects = CI.Effects.BuildFailed,
      description = "Build #123 failed",
      links = Seq(Link("Jenkins build", "http://example.com/123")),
      relevantPeople = Seq(Person(emailAddress = Some("chris.birchall@ovoenergy.com")))
    ))
  }

}
