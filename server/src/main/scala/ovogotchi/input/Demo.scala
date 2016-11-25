package ovogotchi.input

import akka.actor.ActorRef
import ovogotchi.emotion.EmotionEngineV2

object Demo {

//  def buildFailed(emotionEngine: ActorRef): Unit = {
//    emotionEngine ! HandleEvent(InputEvent(
//      source = "CI tool",
//      effects = CI.Effects.BuildFailed,
//      description = "Build #123 failed",
//      links = Seq(Link("Jenkins build", "http://example.com/123")),
//      relevantPeople = Seq(Person(emailAddress = Some("chris.birchall@ovoenergy.com")))
//    ))
//  }

  def buildFailed(emotionEngine: ActorRef): Unit = {
    import EmotionEngineV2.InputEvent

    emotionEngine ! InputEvent()
  }

  def buildSucceeded(emotionEngine: ActorRef): Unit = {
    import EmotionEngineV2.InputEvent

    emotionEngine ! InputEvent()
  }

  def productionDeployment(emotionEngine: ActorRef): Unit = {
    import EmotionEngineV2.InputEvent

    emotionEngine ! InputEvent()
  }

  def openPullRequests(emotionEngine: ActorRef): Unit = {
    import EmotionEngineV2.InputEvent

    emotionEngine ! InputEvent()
  }

}
