import akka.actor.{Actor, Props}

class ParentActor extends Actor {
	override def receive: Receive = {
		case "ping" => sender ! "pong!"
		case CreateChildActorRequest(childName) => context.actorOf(Props[ChildActor], name=childName)
	}
}