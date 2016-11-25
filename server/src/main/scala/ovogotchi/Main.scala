package ovogotchi

import akka.actor.{ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.ws.TextMessage
import akka.http.scaladsl.server.Directives._
import akka.pattern.ask
import akka.stream.ActorMaterializer
import akka.stream.scaladsl.{Sink, Source}
import akka.util.Timeout
import ovogotchi.emotion.{EmotionEngineV2, Personality}
import ovogotchi.input.Demo
import ovogotchi.output.WebsocketClients
import ovogotchi.output.WebsocketClients.AddClient

import scala.concurrent.duration._

object Main extends App {

  implicit val system = ActorSystem("ovogotchi")
  implicit val materializer = ActorMaterializer()
  // needed for the future flatMap/onComplete in the end
  implicit val executionContext = system.dispatcher
  implicit val timeout = Timeout(1.second)

  val websocketClients = system.actorOf(Props(new WebsocketClients), "websocketClients")
  val personality = Personality(temper = 1, recovery = 1, restingWellnessLow = 50, restingWellnessHigh = 60)
  val engine = system.actorOf(Props(new EmotionEngineV2(personality, websocketClients)), "engine")

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
            |function buildSucceeded() {
            |  var http = new XMLHttpRequest();
            |  http.open("POST", "/demo/build/succeeded", true);
            |  http.send();
            |}
            |function buildDeployed() {
            |  var http = new XMLHttpRequest();
            |  http.open("POST", "/demo/build/deployed", true);
            |  http.send();
            |}
            |function envDegraded() {
            |  var http = new XMLHttpRequest();
            |  http.open("POST", "/demo/environment/degraded", true);
            |  http.send();
            |}
            |function envOK() {
            |  var http = new XMLHttpRequest();
            |  http.open("POST", "/demo/environment/ok", true);
            |  http.send();
            |}
            |</script>
            |<ul>
            | <li><button onclick="buildFailed()">Build failed in CI tool</button></li>
            | <li><button onclick="buildSucceeded()">Build succeeded in CI tool</button></li>
            | <li><button onclick="buildDeployed()">Build deployed to production</button></li>
            | <li><button onclick="envDegraded()">Production environment degraded</button></li>
            | <li><button onclick="envOK()">Production environment OK</button></li>
            |</ul>
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
        } ~
        path("succeeded") {
          post {
            complete {
              Demo.buildSucceeded(engine)
              "OK"
            }
          }
        } ~
        path("deployed") {
          post {
            complete {
              Demo.productionDeployment(engine)
              "OK"
            }
          }
        }
      } ~
      pathPrefix("environment") {
        path("degraded") {
          post {
            complete {
              Demo.environmentStatus(engine, "Degraded")
              "OK"
            }
          }
        } ~
          path("ok") {
            post {
              complete {
                Demo.environmentStatus(engine, "Ok")
                "OK"
              }
            }
          }
      }
    } ~
    path("healthcheck") {
      get {
        complete("OK")
      }
    } ~
    pathSingleSlash {
      get {
        redirect("/index.html", StatusCodes.PermanentRedirect)
      }
    } ~
    getFromResourceDirectory("frontend")


  val bindingFuture = Http().bindAndHandle(route, "0.0.0.0", 8080)

  system.scheduler.scheduleOnce(10000 milliseconds) {
    println("FAILED YOOO")
    Demo.buildFailed(engine)
  }

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
//  StdIn.readLine() // let it run until user presses return
//  bindingFuture
//    .flatMap(_.unbind()) // trigger unbinding from the port
//    .onComplete(_ => system.terminate()) // and shutdown when done
  
}
