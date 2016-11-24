package ovogotchi.slackbot

import ovogotchi.emotion.EmotionalState

sealed trait Messages

case class SendMessage(user: Option[String], message: String, channel: String) extends Messages

case class NotifiableState(emotion: EmotionalState) extends Messages

case class MessageReceived(message: String, user: String, channel: String) extends Messages

sealed trait QuestionAsked

case object How extends QuestionAsked
case object Why extends QuestionAsked
case object What extends QuestionAsked
