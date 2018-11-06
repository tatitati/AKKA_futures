package actors.RouterActor

import akka.actor.Actor

class Actor1 extends Actor {
	override def receive: Receive = {
		case "identify" => sender ! "I'm Actor1"
	}
}