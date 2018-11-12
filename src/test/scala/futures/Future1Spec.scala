package Futures

import org.scalatest.{FunSuite, Matchers, WordSpec}

import scala.concurrent.Future
import scala.util.{Failure, Random, Success}
import scala.concurrent.ExecutionContext.Implicits.global

class Future1Spec extends FunSuite with Matchers {
	test("Future1 Can block waiting for the response") {
		val future1 = new Future1()
		val response = future1.simulateBlockingHttpRequest()

		response shouldBe a[String]
		response should be("BLOCKED FUTURE DONE")
	}

	test("We can do a request withoub blocking the future item") {
		val future1 = new Future1()

		val responseFuture = future1.simulateNonBlockingHttpRequest()

		responseFuture shouldBe a[Future[String]]
		val result = responseFuture.onComplete {
			case Success(value) => value should be("NON BLOCKED FUTURE DONE") // otherwise we have to use a blocking Future
			case Failure(e) => e.printStackTrace
		}
	}
}