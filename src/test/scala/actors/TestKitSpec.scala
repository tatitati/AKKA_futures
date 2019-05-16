package actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.testkit.{ImplicitSender, TestKit}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers, WordSpecLike}
import akka.testkit.{ImplicitSender, TestActors, TestKit}
import akka.pattern.ask

class ActorFA extends Actor with ActorLogging {
  override def receive: Receive = {
    case "ping" => {
      println(sender)
      sender ! "whatever"
    }
  }
}

class TestKitSpec()
        extends TestKit(ActorSystem("MySpec"))
        with ImplicitSender
        with WordSpecLike
        with Matchers
        with BeforeAndAfterAll {

  "An Echo actor" must {
    "send back messages unchanged" in {
      var actorFA = system.actorOf(Props[ActorFA])
      // our implicit testActor is used as sender.
      actorFA ! "ping"
      expectMsg("whatever")
    }

  }
}
