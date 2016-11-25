//package ovogotchi.emotion
//
//import akka.actor.{Actor, ActorRef, Props}
//import ovogotchi.output.{WebsocketClients, WebsocketPayload}
//import ovogotchi.slackbot.{TriggerAlert, SlackBot}
//
//import scala.util.Random
//
//class EmotionEngine(websocketClients: ActorRef) extends Actor {
//  import EmotionEngine._
//
//  private var state = State(wellbeing = 50, emotionalState = EmotionalState.Neutral, lastInputEvent = None)
//
//  val events: Seq[InputEvent] = ???
//
//  val slackbot = context.actorOf(Props(new SlackBot(self)), "slackbot")
//
//  private def isAlertWorthy(emotionalState: EmotionalState) = {
//    val alertWorthyEmotions = Seq(EmotionalState.Angry, EmotionalState.Sad)
//
//    alertWorthyEmotions.contains(emotionalState)
//  }
//
//  def receive = {
//    case HandleEvent(event) =>
//      // TODO update emotional state using advanced AI techniques
//      state = State(wellbeing = Random.nextInt(100), emotionalState = EmotionalState.Happy, lastInputEvent = Some(event))
//
//      println(s"Sending updated state to websocket clients: $state")
//      val payload = WebsocketPayload(state.emotionalState, state.wellbeing, None)
//      websocketClients ! WebsocketClients.Broadcast(payload)
//
//      if(isAlertWorthy(state.emotionalState)){
//        slackbot ! TriggerAlert(events)
//      }
//
//      // TODO send slack message if necessary
//    case GetCurrentState =>
//      sender ! state
//
//    case GetStateTrace =>
//      sender ! events
//  }
//}
//
//object EmotionEngine {
//
//  case class HandleEvent(event: InputEvent)
//  case class GetCurrentState()
//
//  case class GetStateTrace()
//
//  case class State(wellbeing: Int, emotionalState: EmotionalState, lastInputEvent: Option[InputEvent])
//
//}
