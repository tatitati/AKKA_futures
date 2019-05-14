package actors.HierarchyActor

import akka.actor.{ActorRef, ActorSystem, PoisonPill, Props}
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.{BeforeAndAfter, FunSuite, Matchers, WordSpec}
import scala.concurrent.Await
import scala.concurrent.duration._

class ChildActorSpec extends FunSuite with Matchers {
	val actorSystem = ActorSystem("ChildActorTest")
	var childActor = actorSystem.actorOf(Props[ChildActor])
	implicit val timeout = Timeout(2.seconds)


	test("actorOf return an ActorRef") {
		childActor shouldBe a [ActorRef]
	}

	test("actors.HierarchyActor.ChildActor ? ping Return pong!") {
		val responseFuture = childActor ? "ping"
		val response = Await.result(responseFuture, timeout.duration)
		response should be("pong!")
	}

	test("actors.HierarchyActor.ChildActor ? <any text different to ping> Return an echo of the same message sent") {
		val responseFuture = childActor ? "whatever"
		val response = Await.result(responseFuture, timeout.duration)
		response should be("whatever")
	}
}