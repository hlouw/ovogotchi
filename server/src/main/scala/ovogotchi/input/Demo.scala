package ovogotchi.input

import akka.actor.ActorRef
import ovogotchi.emotion._

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
    emotionEngine ! PipelineFailure("public-api-paym")
  }

  def buildSucceeded(emotionEngine: ActorRef): Unit = {
    emotionEngine ! PipelineSuccess("public-api-paym")
  }

  def productionDeployment(emotionEngine: ActorRef): Unit = {
    emotionEngine ! ProductionDeployment("public-api-paym")
  }

  def openPullRequests(emotionEngine: ActorRef): Unit = {
    emotionEngine ! OpenPullRequests(Seq(PullRequest("public-api-paym", 5)))
  }

  def closeAllPullRequests(emotionEngine: ActorRef): Unit = {
    emotionEngine ! OpenPullRequests(Seq())
  }

  def environmentStatus(emotionEngine: ActorRef, status: String): Unit = {
    emotionEngine ! EnvironmentStatus("public-api-paym", status)
  }
}
