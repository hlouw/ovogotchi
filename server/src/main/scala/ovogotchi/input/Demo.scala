package ovogotchi.input

import akka.actor.ActorRef
import ovogotchi.emotion.EmotionEngine.HandleEvent

object Demo {

  def buildFailed(emotionEngine: ActorRef): Unit = {
    emotionEngine ! HandleEvent(InputEvent(
      source = "CI tool",
      effects = CI.Effects.BuildFailed,
      description = "Build #123 failed",
      links = Seq(Link("Jenkins build", "http://example.com/123")),
      relevantPeople = Seq(Person(emailAddress = Some("chris.birchall@ovoenergy.com")))
    ))
  }

}
