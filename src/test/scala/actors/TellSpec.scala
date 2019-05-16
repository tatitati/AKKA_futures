package actors

import akka.actor.{Actor, ActorLogging, ActorSystem, DeadLetter, Props}
import org.scalatest.FunSuite

class ActorCA extends Actor with ActorLogging {
  override def receive: Receive = {
    case "ping" => {
      sender ! "whatever" // this msg will go to deadletter
    }
    case DeadLetter(msg, from, to) => {
      println(msg) // => whatever
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

  test("If used !, then deadletters is used in response") {
    val actorSystem = ActorSystem("MyActorSystem")

    var actorCA = actorSystem.actorOf(Props[ActorCA])
    actorSystem.eventStream.subscribe(actorCA, classOf[DeadLetter])

    actorCA ! "ping"
  }
}
