package ovogotchi.emotion

import akka.actor.{ActorRef, FSM, Props}
import ovogotchi.emotion.EmotionEngineV2.{State, StateData}
import ovogotchi.output.{WebsocketClients, WebsocketPayload}
import ovogotchi.slackbot.SlackBot

import scala.concurrent.duration._

class EmotionEngineV2(_personality: Personality, websocketClients: ActorRef) extends FSM[State, StateData]
  with Wellbeing
  with Anger
  with Illness
  with Loneliness {

  import EmotionEngineV2._
  val personality: Personality = _personality

  val slackbot = context.actorOf(Props(new SlackBot(self)), "slackbot")

  startWith(Undecided, StateData(CharacterState(50, EmotionalState.Neutral), EnvironmentState()))

  setTimer("tick", Tick, 1.second, repeat = true)

  when(Undecided) {
    case Event(RefreshState, StateData(c, e)) =>
      if (e.failedBuilds.nonEmpty) {
        goto(Angry) using StateData(c.copy(emotionalState = EmotionalState.Angry, thought = Some("assets/gocd.png")), e)
      } else if (e.envStatus == "Degraded") {
        goto(Ill) using StateData(c.copy(emotionalState = EmotionalState.Ill, thought = Some("assets/aws.png")), e)
      } else if (e.prs.nonEmpty && (e.prs.map(_.ageHours).sum / e.prs.length > 4)) {
        goto(Lonely) using StateData(c.copy(emotionalState = EmotionalState.Lonely, thought = Some("assets/github.png")), e)
      } else {
        goto(WellbeingDriven)
      }

    case Event(Tick, _) => stay
  }

  onTransition {
    case _ -> Undecided =>
      self ! RefreshState

    case _ =>
      val c: CharacterState = nextStateData.charState
      val payload = WebsocketPayload(c.emotionalState, c.wellbeing, c.thought)
      websocketClients ! WebsocketClients.Broadcast(payload)

  }

  onTransition {
    case _ -> Angry =>
      val data: StateData = nextStateData
      if(data.charState.emotionalState == EmotionalState.Angry){
        println("I AM ANGRY!!!!")
        slackbot ! nextStateData
      }
  }

  whenUnhandled {
    case Event(StateRequest, state) =>
      sender ! state
      stay

    case Event(EnvironmentStatus(_, st), StateData(c, e)) =>
      goto(Undecided) using StateData(c, e.copy(envStatus = st))

    case Event(PipelineSuccess(name), StateData(c, e)) =>
      val newFailedBuilds = e.failedBuilds - name
      goto(Undecided) using StateData(c, e.copy(failedBuilds = newFailedBuilds))

    case Event(PipelineFailure(name), StateData(c, e)) =>
      val newFailedBuilds = e.failedBuilds + name
      goto(Undecided) using StateData(c, e.copy(failedBuilds = newFailedBuilds))

    case Event(OpenPullRequests(newPRs), StateData(c, e)) =>
      goto(Undecided) using StateData(c, e.copy(prs = newPRs))
  }

  initialize()
  self ! RefreshState
}

object EmotionEngineV2 {

  case object StateRequest
  case object Tick
  case object RefreshState

  sealed trait State
  case object Undecided extends State
  case object WellbeingDriven extends State
  case object Angry extends State
  case object Ill extends State
  case object Lonely extends State

  case class StateData(charState: CharacterState, envState: EnvironmentState)
  case class EnvironmentState(prs: Seq[PullRequest] = Seq(), failedBuilds: Set[String] = Set.empty[String], envStatus: String = "Ok")
  case class CharacterState(wellbeing: Int, emotionalState: EmotionalState, thought: Option[String] = None)
}