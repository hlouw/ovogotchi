package ovogotchi

import akka.actor.{ActorSystem, Props}
import akka.pattern.ask
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import ovogotchi.emotion.EmotionEngine
import ovogotchi.input.Demo
import ovogotchi.output.WebsocketClients
import ovogotchi.output.WebsocketClients.AddClient
import ovogotchi.slackbot.SlackBot

import scala.io.StdIn
import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem("ovogotchi")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(1.second)

  val websocketClients = system.actorOf(Props(new WebsocketClients), "websocketClients")
  val engine = system.actorOf(Props(new EmotionEngine(websocketClients)), "engine")



  val route =
    path("ws") {
      extractUpgradeToWebSocket { upgradeToWS =>
        complete {
          (websocketClients ? AddClient).mapTo[Source[TextMessage, Any]].map { source =>
            upgradeToWS.handleMessagesWithSinkSource(Sink.ignore, source)
          }
        }
      }
    } ~
    pathPrefix("demo") {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            |<body>
            |<script>
            |function buildFailed() {
            |  var http = new XMLHttpRequest();
            |  http.open("POST", "/demo/build/failed", true);
            |  http.send();
            |}
            |</script>
            |<a href="#" onclick="buildFailed()">Build failed in CI tool!</a>
            |</body>
            |</html>
          """.stripMargin
        ))
      } ~
      pathPrefix("build") {
        path("failed") {
          post {
            complete {
              Demo.buildFailed(engine)
              "OK"
            }
          }
        }
      }
    } ~
    getFromResourceDirectory("frontend")


  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

  println("""
              .-"-.
            .'=^=^='.
           /=^=^=^=^=\
          :^=  OVO  =^;
          |^ GOTCHI! ^|
          :^=^=^=^=^=^:
           \=^=^=^=^=/
            `.=^=^=.'
              `~~~`
  """)
  println(s"Server online at http://localhost:8080/\nPress RETURN to stop...")
  StdIn.readLine() // let it run until user presses return
  bindingFuture
    .flatMap(_.unbind()) // trigger unbinding from the port
    .onComplete(_ => system.terminate()) // and shutdown when done
  
}
