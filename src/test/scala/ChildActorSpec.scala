import akka.actor.{ActorRef, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask

import scala.concurrent.duration._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await

class ChildActorSpec extends WordSpec with Matchers {
	val actorSystem = ActorSystem("ChildActorTest")
	var childActor = actorSystem.actorOf(Props[ChildActor])
	implicit val timeout = Timeout(2.seconds)

	"actorOf" should {
		"return an ActorRef" in {
			childActor shouldBe a [ActorRef]
		}
	}

	"ChildActor ! ping" should {
		"Return pong!" in {
			val responseFuture = childActor ? "ping"

			val response = Await.result(responseFuture, timeout.duration)

			response should be("pong!")
		}
	}
}