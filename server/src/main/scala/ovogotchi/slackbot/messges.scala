package ovogotchi.slackbot

object Messages {

  case class SendMessage(user: Option[String], message: String, channel: String)

  case class SendNotification(emotion: String, channel: String, message: String)

}
