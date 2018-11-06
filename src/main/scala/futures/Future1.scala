package Futures

import scala.concurrent.{Await, Future}
import scala.concurrent.duration._
import scala.concurrent.ExecutionContext.Implicits.global

class Future1 {

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