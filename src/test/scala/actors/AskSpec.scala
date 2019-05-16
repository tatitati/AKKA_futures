package actors

import java.util.concurrent.TimeoutException
import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import org.scalatest.FunSuite
import akka.pattern.ask
import scala.concurrent.{Await, Future}
import akka.util.Timeout
import scala.concurrent.duration._

class ActorDA extends Actor with ActorLogging {
  override def receive: Receive = {
    case "ping" => {
      Thread.sleep(2000) // 2 secs to emulate async response
      sender ! "pong"
    }
  }
}

class AskSpec extends FunSuite {
  test("Ask return a future") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorCA = actorSystem.actorOf(Props[ActorDA])

    implicit val timeout = Timeout(8.seconds)
    val responseFuture = actorCA ? "ping"

    assert(responseFuture.isInstanceOf[Future[_]])
  }

  test("The Future resonse can be resolved") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorCA = actorSystem.actorOf(Props[ActorDA])

    implicit val timeout = Timeout(8.seconds)
    val responseFuture = actorCA ? "ping"

    assert(
      Await.result(responseFuture, timeout.duration) === "pong"
    )

  }

  test("The wait of the Future can finish in Timeout exception") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorCA = actorSystem.actorOf(Props[ActorDA])

    implicit val timeout = Timeout(1.seconds) // not waiting enough for the resonse of the actor
    val responseFuture = actorCA ? "ping"

    val thrown = intercept[TimeoutException] {
      Await.result(responseFuture, timeout.duration)
    }

    assert(thrown.getMessage() === "Futures timed out after [1 second]")
  }
}
