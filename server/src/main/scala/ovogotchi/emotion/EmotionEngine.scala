package ovogotchi.emotion

import akka.actor.Cancellable
import akka.http.scaladsl.model.ws.{Message, TextMessage}
import akka.stream.scaladsl.{Flow, Source}
import ovogotchi.input.InputEvent
import ovogotchi.output.WebsocketPayload
import io.circe.generic.auto._
import io.circe.syntax._

object EmotionEngine {

  def handleEvent(event: InputEvent): Unit = {
    // TODO update emotional state
    // TODO send update to frontend
    // TODO send Slack message if necessary
    println(s"My emotional state is changing! ${event.effects}")
  }

  def addClient(): Source[Message, Cancellable] = {
    import scala.concurrent.duration._
    Source.tick(Duration.Zero, 1.second, TextMessage(WebsocketPayload(EmotionalState.Happy, 42).asJson.spaces2))
  }

}
