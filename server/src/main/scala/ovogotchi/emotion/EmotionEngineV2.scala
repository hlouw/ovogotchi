package ovogotchi.emotion

import akka.actor.{ActorRef, FSM}
import ovogotchi.emotion.EmotionEngineV2.{CharacterState, State}
import ovogotchi.output.{WebsocketClients, WebsocketPayload}

import scala.concurrent.duration._

class EmotionEngineV2(_personality: Personality, websocketClients: ActorRef) extends FSM[State, CharacterState]
  with Wellbeing
  with Anger
  with Illness {

  import EmotionEngineV2._

  val personality: Personality = _personality

  startWith(Undecided, CharacterState(wellbeing = 50, emotionalState = EmotionalState.Neutral))

  setTimer("tick", Tick, 1.second, repeat = true)

  when(Undecided) {
    case Event(RefreshState, d: CharacterState) =>
      goto(WellbeingDriven)

    case Event(Tick, _) =>
      stay
  }

  onTransition {
    case _ -> Undecided =>
      self ! RefreshState

    case _ =>
      val payload = WebsocketPayload(nextStateData.emotionalState, nextStateData.wellbeing, nextStateData.thought)
      websocketClients ! WebsocketClients.Broadcast(payload)
  }

  whenUnhandled {
    case Event(StateRequest, d: CharacterState) =>
      sender ! d
      stay
  }

  initialize()
  self ! RefreshState
}

object EmotionEngineV2 {

  case class InputEvent()
  case object StateRequest
  case object Tick
  case object RefreshState

  sealed trait State
  case object Undecided extends State
  case object WellbeingDriven extends State
  case object Angry extends State
  case object Ill extends State

  case class StateData(charState: CharacterState, envState: EnvironmentState)
  case class EnvironmentState()
  case class CharacterState(wellbeing: Int, emotionalState: EmotionalState, thought: Option[String] = None)
}