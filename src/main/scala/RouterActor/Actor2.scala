package RouterActor

import akka.actor.Actor

class Actor2 extends Actor {
	override def receive: Receive = {
		case "identify" => sender ! "I'm Actor2"
	}
}