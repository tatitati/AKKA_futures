package RouterActor

import HierarchyActor.ChildActor
import akka.actor.{Actor, ActorRef, Props}
import akka.util.Timeout

import scala.concurrent.Await
import scala.concurrent.duration._
import akka.pattern.ask


class RouterActor(actingWith: ActorRef) extends Actor {

	implicit val timeout = Timeout(2.seconds)

	override def receive: Receive = {
		case "speakingWith" => {
			val responseFuture = actingWith ? "identify"
			val response = Await.result(responseFuture, timeout.duration)

			sender ! response
		}
	}
}

