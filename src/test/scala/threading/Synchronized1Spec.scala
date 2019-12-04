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
    // 400000
    // 400000
    // 400000
  }
}
