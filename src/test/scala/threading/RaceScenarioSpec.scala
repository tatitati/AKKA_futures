package threading

import org.scalatest.FunSuite

class RaceScenarioSpec extends FunSuite {
  test("I create a RACE scenario") {
    var num = 0
    def workerIncrement(): Unit = {
      for (_ <- 1 to 100000) {
        num = num + 1
      }
    }

    val t1 = new Thread{
      override def run(): Unit = workerIncrement()
    }
    val t2 = new Thread{
      override def run(): Unit = workerIncrement()
    }
    val t3 = new Thread{
      override def run(): Unit = workerIncrement()
    }
    val t4 = new Thread{
      override def run(): Unit = workerIncrement()
    }

    val threadsPool = List(t1, t2, t3, t4)
    threadsPool.map(_.start())
    threadsPool.map(_.join())

    println(num)

    // OUTPUT:
    // 115789
    // 15885
    // 13383
  }
}
