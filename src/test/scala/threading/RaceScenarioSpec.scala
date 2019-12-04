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

    val threadsPool = List.tabulate(4){ _ =>
      new Thread{
        override def run(): Unit = workerIncrement()
      }
    }

    threadsPool.map(_.start())
    threadsPool.map(_.join())

    println(num)

    // OUTPUT:
    // 115789
    // 15885
    // 13383
  }
}
