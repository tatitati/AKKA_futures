package HierarchyActor

import akka.actor.{ActorRef, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

class ChildActorSpec extends WordSpec with Matchers {
	val actorSystem = ActorSystem("ChildActorTest")
	var childActor = actorSystem.actorOf(Props[ChildActor])
	implicit val timeout = Timeout(2.seconds)

	"actorOf" should {
		"return an ActorRef" in {
			childActor shouldBe a [ActorRef]
		}
	}

	"HierarchyActor.ChildActor ? ping" should {
		"Return pong!" in {
			val responseFuture = childActor ? "ping"

			val response = Await.result(responseFuture, timeout.duration)

			response should be("pong!")
		}
	}

	"HierarchyActor.ChildActor ? <any text different to ping>" should {
		"Return an echo of the same message sent" in {
			val responseFuture = childActor ? "whatever"

			val response = Await.result(responseFuture, timeout.duration)

			response should be("whatever")
		}
	}
}