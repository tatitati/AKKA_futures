package HierarchyActor

import akka.actor.{ActorRef, ActorSelection, ActorSystem, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.{Matchers, WordSpec}

import scala.concurrent.Await
import scala.concurrent.duration._

class ParentActorSpec extends WordSpec with Matchers {
	implicit val timeout = Timeout(2.seconds)
	val actorSystem = ActorSystem("actorTestSystem")
	var parentActor = actorSystem.actorOf(Props[ParentActor], name="myParentActor")

	"ActorRef returned when creating an actor" should {
		"has a path and name even if we don't assigned it a name explicitily" in {
			val parentActorUnnamed = actorSystem.actorOf(Props[ParentActor])

			parentActorUnnamed shouldBe a [ActorRef]
			parentActorUnnamed.path.toString should be("akka://actorTestSystem/user/$a")
			parentActorUnnamed.path.name should be("$a")
		}
	}

	"HierarchyActor.ParentActor ? ping" should {
		"Return pong!" in {
			val responseFuture = parentActor ? "ping"

			val response = Await.result(responseFuture, timeout.duration)

			response should be("pong!")
		}
	}

	"HierarchyActor.ParentActor ! HierarchyActor.CreateChildActorRequest(<nameChild>)" should {
		"Create a child actor" in {
			parentActor ! new CreateChildActorRequest("child1")

			// get reference to the new created child actor
			val childActorCreated = actorSystem.actorSelection("/user/myParentActor/child1")
			val responseChildFuture = childActorCreated ? "ping"
			val responseChild = Await.result(responseChildFuture, timeout.duration)

			// assert that child has been created
			responseChild should be("pong!")
			responseChild shouldBe a [String]

			childActorCreated shouldBe a [ActorSelection]
		}
	}
}