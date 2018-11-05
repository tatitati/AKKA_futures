import akka.actor.{ActorRef, ActorSelection, ActorSystem, Props}
import akka.util.Timeout
import akka.pattern.ask
import scala.concurrent.duration._
import org.scalatest.{Matchers, WordSpec}
import scala.concurrent.Await

class ParentActorSpec extends WordSpec with Matchers {
	implicit val timeout = Timeout(2.seconds)
	val actorSystem = ActorSystem("actorTestSystem")
	var parentActor = actorSystem.actorOf(Props[ParentActor], name="myParentActor")

	"ActorRef returned when createing an actor" should {
		"has a path and name even if we don't assigned it explicitily" in {
			val parentActorUnnamed = actorSystem.actorOf(Props[ParentActor])

			parentActorUnnamed shouldBe a [ActorRef]
			parentActorUnnamed.path.toString should be("akka://actorTestSystem/user/$a")
			parentActorUnnamed.path.name should be("$a")
		}
	}

	"ParentActor ? ping" should {
		"Return pong!" in {
			val responseFuture = parentActor ? "ping"

			val response = Await.result(responseFuture, timeout.duration)

			response should be("pong!")
		}
	}

	"ParentActor ! CreateChildActorRequest(<nameChild>)" should {
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