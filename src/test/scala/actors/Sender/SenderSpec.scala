//package actors.Sender
//
//import akka.actor.{Actor, ActorRef, ActorSystem, Props}
//import org.scalatest.FunSuite
//import akka.pattern.ask
//import akka.util.Timeout
//import scala.concurrent.Await
//import scala.concurrent.duration._
//
//case class Msg(text: String)
//
//class SenderActor(val receiverActor: ActorRef) extends Actor {
//  implicit val timeout = Timeout(20.seconds)
//
//  override def receive: Receive = {
//
//    case Msg(text) => {
//      println("Sending: " + self.path.name)
//      val responseFuture = receiverActor ? Msg(self.path.name)
//
//      val responseResolved = Await.result(responseFuture, timeout.duration)
//      println("Response in " + self.path.name + ": " + responseResolved)
//
//    }
//  }
//}
//
//class ReceiverActor extends Actor {
//  override def receive: Receive = {
//    case Msg(text) => {
//      println(self.path.name + ": received msg from: " + sender.path.name + ". sleeping 1 sec....")
//      Thread.sleep(1000) // 5 secs
//      sender ! text
//    }
//  }
//}
//
//
//class SenderSpec extends FunSuite {
//  val actorSystem = ActorSystem("ChildActorTest")
//  var receiverActor = actorSystem.actorOf(Props[ReceiverActor], "receiver")
//  var senderActor1 = actorSystem.actorOf(Props(new SenderActor(receiverActor)), "sender1")
//  var senderActor2 = actorSystem.actorOf(Props(new SenderActor(receiverActor)), "sender2")
//
//  test("we send a few messages to the sender") {
//    senderActor1 ! Msg("ONE")
//    senderActor2 ! Msg("TWO")
//  }
//}
