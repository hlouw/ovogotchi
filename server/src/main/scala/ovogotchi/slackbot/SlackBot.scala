package ovogotchi.slackbot

import ovogotchi.emotion.EmotionEngine._
import akka.actor.{Actor, ActorRef, Props}
import com.typesafe.config.ConfigFactory
import akka.pattern.ask
import akka.util.Timeout
import ovogotchi.slackbot.Responders._
import slack.api.SlackApiClient
import slack.models.Channel

import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

class SlackBot(emotionEngine: ActorRef) extends Actor {

  val actorSystem   = context.system
  val config        = ConfigFactory.load()
  val token         =  config.getString("slack.apiToken")
  val eventStream   = actorSystem.actorOf(Props(new EventStream(self)))
  val client        = SlackApiClient(token)

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

  private def processEmotionalResponse(state: State, input: QuestionAsked) = {
    println("processing emotional response")
    responses.get(input).map(a => a(state)).get
  }

  private def generateResponse(message: String): Future[String] = {
    println("Generating Response")
    message match {
      case msg if msg.contains("how")  || msg.contains("How")  => println("case 1" ); (emotionEngine ? GetCurrentState).mapTo[State].map(res => processEmotionalResponse(res, How))
      case msg if msg.contains("what") || msg.contains("What") => println("case 2" ); (emotionEngine ? GetCurrentState).mapTo[State].map(res => processEmotionalResponse(res, What))
      case msg if msg.contains("why")  || msg.contains("Why")  => println("case 3" ); (emotionEngine ? GetCurrentState).mapTo[State].map(res => processEmotionalResponse(res, Why))
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

  override def receive: Receive = {
    case msg: SendMessage           => sendMessage(msg.channel, msg.message, msg.user)
    case ntf: SendNotification      => sendMessage(ntf.channel, "@channel" + ntf.message, Some("channel"))
    case received: MessageReceived  => parseReceivedMessage(received)
    case state: State               =>
  }
}
