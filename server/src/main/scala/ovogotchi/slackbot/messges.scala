package ovogotchi.slackbot

import ovogotchi.input.InputEvent

sealed trait Messages

case class SendMessage(user: Option[String], message: String, channel: String) extends Messages

case class TriggerAlert(event: Seq[InputEvent]) extends Messages

case class MessageReceived(message: String, user: String, channel: String) extends Messages

sealed trait QuestionAsked

case object How extends QuestionAsked
case object Why extends QuestionAsked
case object What extends QuestionAsked
