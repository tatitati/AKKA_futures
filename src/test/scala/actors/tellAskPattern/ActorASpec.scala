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

  test("TELL is used when no response is expected, so even if ActorA reply inmediately, no response is returned") {
    val response = actorA ! "identifyWithTell"

    assert(response.isInstanceOf[Unit], "response is always empty with tell(!), even if the actor reply quick")
  }

  test("ASK is used when a response from actorA is needed. ASK returns immedietely a promise") {
    implicit val timeout = Timeout(8.seconds)
    val responseFuture = actorA ? "identifyWithAsk"

    assert(responseFuture.isInstanceOf[Future[Try[Any]]])

    val responseResolved = Await.result(responseFuture, timeout.duration)
    assert(responseResolved === "I'm ActorA", "If we wait enough, the the promise can be resolved and we can get the value")
  }

  test("ASK: actorA sleep 5 seconds, but we will wait 3 secs to try to resolve the future, which will produce a timeout") {

    implicit val timeout = Timeout(3.seconds) //
    val responseFuture = actorA ? "identifyWithAskAndBigDelay"

    assert(responseFuture.isInstanceOf[Future[Try[Any]]])

    val thrown = intercept[TimeoutException] {
      Await.result(responseFuture, timeout.duration)
    }

    assert(thrown.getMessage() === "Futures timed out after [3 seconds]")
  }
}