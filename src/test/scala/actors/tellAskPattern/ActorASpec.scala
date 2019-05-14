package actors.tellAskPattern

import java.util.concurrent.TimeoutException
import akka.actor.{ActorSystem, Props}
import org.scalatest.{FunSuite, Matchers}
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.util.{Failure, Success, Try}

class ActorASpec extends FunSuite with Matchers {
  val actorSystem = ActorSystem("ChildActorTest")
  var actorA = actorSystem.actorOf(Props[ActorA])

  test("Tell is used when no response is expected, so even if ActorA reply inmediately, no response is returned") {
    val response = actorA ! "identifyWithTell"

    assert(response.isInstanceOf[Unit], "response is always empty with tell(!), even if the actor reply quick")
  }

  test("Ask return futures when no delay exists in actor response") {
    implicit val timeout = Timeout(8.seconds)
    val responseFuture = actorA ? "identifyWithAsk"

    assert(responseFuture.isInstanceOf[Future[Try[Any]]], "response is always a future with  ask(?). is used when a response is expected")

    val responseResolved = Await.result(responseFuture, timeout.duration)
    assert(responseResolved === "I'm ActorA", "actorA response immediately, so the promise is resolved in less than 1 sec. No timeout")
  }

  test("Ask return futures also when big delays happen") {

    implicit val timeout = Timeout(3.seconds) // actorA sleep 5 seconds, so this should create a timeout
    val responseFuture = actorA ? "identifyWithAskAndBigDelay" // Doesnt matter the delay, a promise is immedietely returned

    assert(responseFuture.isInstanceOf[Future[Try[Any]]], "with ask, the actorA returns immedietly a promise.")

    val thrown = intercept[TimeoutException] {
      // a timeout exception is arised because we wait 3 secs, however actorA takes 5 secs
      Await.result(responseFuture, timeout.duration)
    }

    assert(thrown.getMessage() === "Futures timed out after [3 seconds]", "The actorA has a delay of 5 seconds")
  }
}