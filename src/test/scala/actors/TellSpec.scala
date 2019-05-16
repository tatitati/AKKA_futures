package actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import org.scalatest.FunSuite

class ActorCA extends Actor with ActorLogging {
  override def receive: Receive = {
    case "ping" => {
      sender ! "whatever"
    }
  }
}

class TellSpec extends FunSuite {
  
  test("Tell/Bang/! never receives a response") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorCA = actorSystem.actorOf(Props[ActorCA])

    val response = actorCA ! "ping"

    assert(response.isInstanceOf[Unit])
  }
}
