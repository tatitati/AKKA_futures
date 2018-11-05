import akka.actor.Actor

class ChildActor extends Actor {
	override def receive: Receive = {
		case "ping" => sender ! "pong!"
		case echoMsg => sender ! echoMsg
	}
}