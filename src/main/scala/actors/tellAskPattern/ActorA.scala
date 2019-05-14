package actors.tellAskPattern

import akka.actor.Actor

class ActorA extends Actor {
  override def receive: Receive = {
    case "identifyWithTell" => {
      sender ! "I'm ActorA"
    }

    case "identifyWithAsk" => {
      sender ! "I'm ActorA"
    }

    case "identifyWithAskAndBigDelay" => {
      Thread.sleep(5000) // 5 secs
      println("ActorA: delay of 5secs complete")
      sender ! "I'm ActorA"
    }
  }
}