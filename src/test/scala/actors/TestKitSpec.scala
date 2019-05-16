package actors

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import org.scalatest.{BeforeAndAfterAll, FunSuite, Matchers, WordSpecLike}
import akka.testkit.{ImplicitSender, TestActors, TestKit}

class ActorFA extends Actor with ActorLogging {
  override def receive: Receive = {
    case "ping" => {
      println(sender) // Actor[akka://MySpec/system/testActor-1#-2109583606
      sender ! "whatever1"
      sender ! "whatever2"
      sender ! "whatever3"
    }
  }
}

class ActorFB extends Actor with ActorLogging {
  override def receive: Receive = {
    case "ping" => {
      Thread.sleep(1000) // 1 sec
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

  override def afterAll: Unit = {
    TestKit.shutdownActorSystem(system)
  }

  "Given an implicit test actor included in our testkit" must {
    "I can be explicit about the order and msgs received" in {
      // the testKit implicit testActor will send a message to ActorFA.
      var actorFA = system.actorOf(Props[ActorFA])
      // our implicit testActor is used as sender.
      actorFA ! "ping"
      expectMsg("whatever1")
      expectMsg("whatever2")
      expectMsg("whatever3")
    }

    "Can indicate how many messages I want to receive" in {
      // the testKit implicit testActor will send a message to ActorFA.
      var actorFA = system.actorOf(Props[ActorFA])
      // our implicit testActor is used as sender.
      actorFA ! "ping"
      receiveN(2)
      expectMsg("whatever3")
    }

    "I can receive even if exists a deleay (max of 3 secs)" in {
      var actorFB = system.actorOf(Props[ActorFB])
      actorFB ! "ping"
      expectMsg("whatever")
    }
  }
}
