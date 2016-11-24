package ovogotchi.emotion

import akka.actor.{Actor, ActorRef, Props}
import ovogotchi.input.InputEvent
import ovogotchi.output.{WebsocketClients, WebsocketPayload}
import ovogotchi.slackbot.SlackBot

import scala.util.Random

class EmotionEngine(websocketClients: ActorRef) extends Actor {
  import EmotionEngine._

  private var state = State(wellbeing = 50, emotionalState = EmotionalState.Neutral, lastInputEvent = None)

  val slackbot = context.actorOf(Props(new SlackBot(self)), "slackbot")

  def receive = {
    case HandleEvent(event) =>
      // TODO update emotional state using advanced AI techniques
      state = State(wellbeing = Random.nextInt(100), emotionalState = EmotionalState.Happy, lastInputEvent = Some(event))

      println(s"Sending updated state to websocket clients: $state")
      val payload = WebsocketPayload(state.emotionalState, state.wellbeing, None)
      websocketClients ! WebsocketClients.Broadcast(payload)

      // TODO send slack message if necessary
    case GetCurrentState =>
      sender ! state
  }

}

object EmotionEngine {

  case class HandleEvent(event: InputEvent)
  case class GetCurrentState()

  case class State(wellbeing: Int, emotionalState: EmotionalState, lastInputEvent: Option[InputEvent])

}
