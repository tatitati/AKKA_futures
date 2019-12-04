package threading

import org.scalatest.FunSuite

class Synchronized1Spec extends FunSuite {

  test("Synchronization with Lock scenario") {
    var num = 0
    def workerIncrement(): Unit = this.synchronized {
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
    // 400000
    // 400000
    // 400000
  }
}
