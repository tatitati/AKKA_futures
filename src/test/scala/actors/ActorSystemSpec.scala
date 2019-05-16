package actors

import akka.actor.{Actor, ActorPath, ActorRef, ActorSystem, Identify, InvalidActorNameException, Props}
import org.scalatest.FunSuite

// very simple
class ActorEA extends Actor {
  override def receive: Receive = {
    case "ping" => println("asdf")
  }
}


class ActorSystemSpec extends FunSuite {

  test("Create an actor system") {
    val actorSystem = ActorSystem("MyActorSystem")

    assert(actorSystem.isInstanceOf[ActorSystem])
    assert(actorSystem.name === "MyActorSystem")
    assert(actorSystem.toString === "akka://MyActorSystem")
  }

  test("Create an actor in an actor system") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorEA = actorSystem.actorOf(Props[ActorEA])

    assert(actorEA.isInstanceOf[ActorRef])
    assert(actorEA.path.toString === "akka://MyActorSystem/user/$a")
  }

  test("Create [multiple] actors in an actor system") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorEA1 = actorSystem.actorOf(Props[ActorEA])
    var actorEA2 = actorSystem.actorOf(Props[ActorEA])

    assert(actorEA1.path.toString === "akka://MyActorSystem/user/$a")
    assert(actorEA2.path.toString === "akka://MyActorSystem/user/$b")
  }
}
