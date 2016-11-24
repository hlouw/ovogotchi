package ovogotchi.slackbot

import akka.actor.{Actor, ActorSystem}
import com.typesafe.config.ConfigFactory
import slack.models.Message
import slack.rtm.SlackRtmClient

class EventStream extends Actor {
  val config      = ConfigFactory.load()
  val token       =  config.getString("slack.apiToken")
  val eventClient = SlackRtmClient(token)

  val parent = context.parent

  eventClient.onMessage { (message: Message) =>
    println(s"User: ${message.user}, Message: ${message.text}")
  }

  override def receive: Receive = {
    case any => println(s"Received a message on event stream: $any")
  }
}
