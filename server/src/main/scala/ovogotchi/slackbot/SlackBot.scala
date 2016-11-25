package ovogotchi.slackbot

import akka.actor.{Actor, ActorRef, Props}
import akka.pattern.ask
import akka.util.Timeout
import com.typesafe.config.ConfigFactory
import ovogotchi.emotion.EmotionEngineV2.{StateData, StateRequest}
import ovogotchi.slackbot.Responders._
import slack.api.SlackApiClient
import slack.models.Channel

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.concurrent.duration._


class SlackBot(emotionEngine: ActorRef) extends Actor {

  val actorSystem   = context.system
  val config        = ConfigFactory.load()
  val token         =  config.getString("slack.apiToken")
  val alertChannel  =  config.getString("slack.alertChannel")
  val eventStream   = actorSystem.actorOf(Props(new EventStream(self)))
  val client        = SlackApiClient(token)
  var lastAlert     = 0l

  implicit val timeout = Timeout(1.second)

  def sendMessage(channel: String, message: String, user: Option[String] = None) = {
    client.postChatMessage(
      channel,
      message,
      user,
      Some(true)
    )
  }

  def findChannel(client: SlackApiClient)(distinguishChannel: Channel => Boolean) = {
    client.listChannels().map(_.filter(distinguishChannel).head)
  }

  def findUser(userId: String) = {
    client.getUserInfo(userId)
  }

  private def processEmotionalResponse(state: StateData, input: QuestionAsked) = {
    println("processing emotional response")
    responses.get(input).map(a => a(state)).get
  }

  private def generateResponse(message: String): Future[String] = {
    println("Generating Response")
    message match {
      case msg if msg.contains("how")  || msg.contains("How")  => println("case 1" ); (emotionEngine ? StateRequest).mapTo[StateData].map(res => processEmotionalResponse(res, How))
      case msg if msg.contains("what") || msg.contains("What") => println("case 2" ); (emotionEngine ? StateRequest).mapTo[StateData].map(res => processEmotionalResponse(res, What))
      case msg if msg.contains("why")  || msg.contains("Why")  => println("case 3" ); (emotionEngine ? StateRequest).mapTo[StateData].map(res => processEmotionalResponse(res, Why))
      case _ => println("Failed"); Future.successful("nooooooooo!")
    }
  }


  private def parseReceivedMessage(received: MessageReceived) = {
    println("Parsing received message")
    for {
      channel <- findChannel(client)(_.id == received.channel)
      user    <- findUser(received.user)
      res     <- generateResponse(received.message)
      msg     <- sendMessage(channel.name, s"Hey ${user.name}, $res")
    } yield msg
  }

  private def triggerAlert(state: StateData) = {
    val alertMsg = alerts.get(state.charState.emotionalState).map(f => f(state.envState))
    if(shouldAlert){
      alertMsg.map{ alert =>
        sendMessage(alertChannel, "!!!ALERT!!! " + alert)
      }
    }
  }

  private def shouldAlert: Boolean = {
    val res = if(lastAlert == 0) {lastAlert=System.currentTimeMillis(); true}
    else{
      val current = System.currentTimeMillis()
      if((current - lastAlert) > 300000 ){System.currentTimeMillis(); true}
      else false
    }
    println(s"Should alert: ${res}")
    res
  }

  override def receive: Receive = {
    case msg: SendMessage           => sendMessage(msg.channel, msg.message, msg.user)
    case state: StateData           => triggerAlert(state)
    case received: MessageReceived  => parseReceivedMessage(received)
  }
}
