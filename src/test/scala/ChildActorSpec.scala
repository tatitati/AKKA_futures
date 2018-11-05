import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await

class ChildActorSpec extends WordSpec with Matchers {
	"ChildActor ! ping" should {
		"Return pong!" in {
			val actorSystem = ActorSystem("ChildActorTest")
			var childActor = actorSystem.actorOf(Props[ChildActor])
			implicit val timeout = Timeout(2.seconds)

			val responseFuture = childActor ? "ping"

			val response = Await.result(responseFuture, timeout.duration)

			response should be("pong!")
		}
	}
}