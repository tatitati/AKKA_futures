package actors

import akka.actor.{Actor, ActorLogging, ActorRef, ActorSystem, Props}
import org.scalatest.FunSuite

class ActorBA extends Actor with ActorLogging {
  override def receive: Receive = {
    case "ping" => {
      log.info("loggin something")
      log.warning("loggin something")
    }
  }
}

class LoggingInActorSpec extends FunSuite {
  test("Display some [INFO] ...... and some [WARNING] ..... in the standard output") {
    val actorSystem = ActorSystem("MyActorSystem")
    var actorBA = actorSystem.actorOf(Props[ActorBA])

    actorBA ! "ping"
  }
}
