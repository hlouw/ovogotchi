package ovogotchi.slackbot

import akka.actor.{Actor, ActorRef}
import com.typesafe.config.ConfigFactory
import slack.models.Message
import slack.rtm.SlackRtmClient


class EventStream(parent: ActorRef) extends Actor {

  val config      = ConfigFactory.load()
  val token       =  config.getString("slack.apiToken")
  val eventClient = SlackRtmClient(token)

  val botDetails = eventClient.state.self

  private def isvalidMessage(message: Message) = {
    val toMe = message.text contains (s"${botDetails.id}")
    val fromSomeoneElse = message.user != botDetails.id
    println(toMe + " " + fromSomeoneElse)
    toMe && fromSomeoneElse
  }

  eventClient.onMessage { (message: Message) =>
    println(s"Received: $message")
    if(isvalidMessage(message)){
      parent ! MessageReceived(message.text, message.user, message.channel)
    }
  }

  override def receive: Receive = {
    case any => println(s"Received a message on event stream: $any")
  }
}
