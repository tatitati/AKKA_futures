package actors.HierarchyActor

import akka.actor.{Actor, Props}

// this actor creates another actor (a child one)
class ParentActor extends Actor {
	override def receive: Receive = {
		case "ping" => sender ! "pong!"
		case CreateChildActorRequest(childName) => context.actorOf(Props[ChildActor], name=childName)
	}
}