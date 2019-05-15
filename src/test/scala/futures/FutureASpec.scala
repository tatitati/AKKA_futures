package Futures

import org.scalatest.{FunSuite, Matchers, WordSpec}
import scala.util.{Failure, Random, Success}
import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class FutureA {

	def simulateBlockingHttpRequest(): String = {
		val responseFuture = Future {
			Thread.sleep(500) // 0.5 secs
			"BLOCKED FUTURE DONE"
		}

		Await.result(responseFuture, 1 second)
	}

	def simulateNonBlockingHttpRequest(): Future[String] = {
		Future {
			Thread.sleep(500) // 0.5 secs
			"NON BLOCKED FUTURE DONE"
		}
	}
}

class FutureASpec extends FunSuite with Matchers {
	test("Future1 Can block waiting for the response") {
		val future1 = new FutureA()
		val response = future1.simulateBlockingHttpRequest()

		response shouldBe a[String]
		response should be("BLOCKED FUTURE DONE")
	}

	test("We can do a request withoub blocking the future item") {
		val future1 = new FutureA()

		val responseFuture = future1.simulateNonBlockingHttpRequest()

		responseFuture shouldBe a[Future[String]]
		val result = responseFuture.onComplete {
			case Success(value) => value should be("NON BLOCKED FUTURE DONE") // otherwise we have to use a blocking Future
			case Failure(e) => e.printStackTrace
		}
	}
}