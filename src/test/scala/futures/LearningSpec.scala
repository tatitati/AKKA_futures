package futures

import org.scalatest.FunSuite
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.{Failure, Success}

class LearningSpec extends FunSuite {

  val r = scala.util.Random
  val a: List[Int] = (1 to 20).toList
  val b: List[Future[Int]] = a.map{n =>
    val c: Future[Int] = Future{
      val waitTime: Int = (1000 to 10000 by 1000)(r.nextInt(10))
      println(Thread.currentThread.getName() + " | working with: " + n + " waiting: " + waitTime)

      Thread.sleep(waitTime) // random thread bussy time
      n * 100
    }

    c onComplete{
      case Success(value) => value
      case Failure(_) => 0
    }

    c
  }

  test("I can emulate a series of futures/threads with random bussy times"){
    val resultSequential: List[Int] = b.map(f =>
      Await.result(f, 100 seconds)
    )

    println(resultSequential)
  }

  test("A future can have multiple callbacks"){
    val a = Future{
      Thread.sleep(5000)
      println("this is the FUTURE")
      1 + 1
    }

    a onComplete{
      case Success(number) => println("FIRST callback: " + number)
      case Failure(_) => println("fucked")
    }

    a onComplete{
      case Success(number) => println("SECOND callback: " + number)
      case Failure(_) => println("fucked")
    }

    Thread.sleep(50000)
  }

  test("Can convert a list of futures in a future of lists"){
    val a = List(1, 2, 3, 4, 5, 6)
    val b: List[Future[Int]] = a.map{ n =>
      Future{n * 10}
    }
    val c: Future[List[Int]] = Future.sequence(b)
  }

  test("Callbacks are attached when returning a Future?: NO") {
    def justwait(number: Int): Future[Int] = {
      val fut = Future{
        Thread.sleep(1000)
        number+1
      }

      fut foreach { n =>
        n * 10
      }

      fut
    }

    val result = justwait(5)
    val total = Await.result(result, 5 seconds)

    assert(6 == total)
  }
}
