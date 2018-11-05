import akka.actor.{ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import org.scalatest.{Matchers, WordSpec}


import scala.concurrent.Await

class ParentActorSpec extends WordSpec with Matchers {
	"ParentActor ! ping" should {
		"Return pong!" in {
			val actorSystem = ActorSystem("ChildActorTest")
			var parentActor = actorSystem.actorOf(Props[ParentActor])
			implicit val timeout = Timeout(2.seconds)

			val responseFuture = parentActor ? "ping"

			val response = Await.result(responseFuture, timeout.duration)

			response should be("pong!")
		}
	}

	"ParentActor ! createChild(<nameChild>)" should {
		"Create a child actor" in {
			// create parent actor
			val actorSystem = ActorSystem("ChildActorTest")
			var parentActor = actorSystem.actorOf(Props[ParentActor], name="myParentActor")
			implicit val timeout = Timeout(2.seconds)

			// order to parent actor to create a child actor
			parentActor ! new NameIdentifier("child1")

			// get reference to the new created child actor
			val childActorCreated = actorSystem.actorSelection("/user/myParentActor/child1")
			val responseChildFuture = childActorCreated ? "ping"
			val responseChild = Await.result(responseChildFuture, timeout.duration)

			// assert that child has been created
			responseChild should be("pong!")
		}
	}
}