package actors.Sender

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import org.scalatest.FunSuite
import akka.pattern.ask
import akka.util.Timeout
import scala.concurrent.Await
import scala.concurrent.duration._

class ActorC extends Actor {
  override def receive: Receive = {
    case "send" => {
      sender ! self.path.name
    }
  }
}



class ActorNamesSpec extends FunSuite {
  val actorSystem = ActorSystem("ChildActorTest")
  implicit val timeout = Timeout(20.seconds)

  test("An actor know the name of an actor") {
    var senderActor1 = actorSystem.actorOf(Props[ActorC], "actor-c")

    val responseFuture = senderActor1 ? "send"
    val responseResolved = Await.result(responseFuture, timeout.duration)

    assert(responseResolved === "actor-c")
  }
}
