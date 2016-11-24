package ovogotchi.output

import akka.actor.{Actor, ActorRef}
import akka.http.scaladsl.model.ws.TextMessage
import akka.stream.{Materializer, OverflowStrategy}
import akka.stream.scaladsl.Source
import io.circe.generic.auto._
import io.circe.syntax._

import scala.collection.mutable.ArrayBuffer

class WebsocketClients(implicit materializer: Materializer) extends Actor {
  import WebsocketClients._

  private val clients = ArrayBuffer.empty[ActorRef]

  def receive: Receive = {
    case AddClient =>
      println("Adding WebSocket client")
      val source = Source.actorRef[TextMessage](100, OverflowStrategy.dropHead)
          .mapMaterializedValue(actor => clients.append(actor))
      sender ! source
    case Broadcast(payload) =>
      val message = TextMessage(payload.asJson.spaces2)
      println(s"Broadcasting message: $message")
      clients.foreach( actor => {
        println(s"Sending payload to actor $actor")
        actor ! message
      })
  }
}

object WebsocketClients {

  case object AddClient
  case class Broadcast(payload: WebsocketPayload)

}
