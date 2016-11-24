package ovogotchi

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.server.Directives._
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.Sink
import ovogotchi.emotion.EmotionEngine
import ovogotchi.input.Demo

import scala.io.StdIn

object Main extends App {

  implicit val system = ActorSystem("ovogotchi")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher

  val route =
    pathSingleSlash {
      get {
        complete(HttpEntity(ContentTypes.`text/html(UTF-8)`,
          """
            |<html>
            |<body>
            |<div id="events"></div>
            |<script>
            |var div = document.getElementById("events");
            |var socket = new WebSocket("ws://" + window.location.hostname + ":8080/ws")
            |socket.onmessage = function(event) {
            |  console.log(event.data);
            |  var p = document.createElement("p");
            |  p.innerText = event.data;
            |  div.appendChild(p);
            |}
            |</script>
            |</body>
            |</html>
          """.stripMargin
        ))
      }
    } ~
    {
      path("ws") {
        extractUpgradeToWebSocket { upgradeToWS =>
          complete(upgradeToWS.handleMessagesWithSinkSource(Sink.ignore, EmotionEngine.addClient()))

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
              Demo.buildFailed()
              "OK"
            }
          }
        }
      }
    }

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
