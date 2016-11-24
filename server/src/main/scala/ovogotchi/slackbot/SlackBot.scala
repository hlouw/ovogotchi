package ovogotchi.slackbot

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.ConfigFactory
import ovogotchi.slackbot.Messages._
import slack.api.SlackApiClient
import slack.models.Channel

import scala.concurrent.ExecutionContext.Implicits.global

class SlackBot()(implicit val actorSystem: ActorSystem) extends Actor {

  val config  = ConfigFactory.load()
  val token   =  config.getString("slack.apiToken")
  val botClient: SlackApiClient = SlackApiClient(token)

  val res = botClient.listChannels() // => Future[Seq[Channel]]

  val eventStream = actorSystem.actorOf(Props[EventStream])


  def sendMessage(channel: String, message: String, user: Option[String] = None) = {
    botClient.postChatMessage(
      channel,
      message,
      user
    )
  }

  def findChannel(channels: Seq[Channel], name: String) = {
    val res = channels.filter(_.name == name)
    println(s"found $res")
    println(res.length)
    res.map(_.id).head
  }

  override def receive: Receive = {
    case msg: SendMessage       => sendMessage(msg.channel, msg.message, msg.user)
    case ntf: SendNotification  => sendMessage(ntf.channel, ntf.message, Some("channel"))
  }
}
