package threading

import org.scalatest.FunSuite

class SynchronizedSpec extends FunSuite {

  test("writes to *num in a synchronized way to avoid race scenario") {
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
  }

  test("A Thread.sleep() inside of a synchronized doesnt produce a change to another thread becauase it owns the lock during the sleep period") {

    def workerIncrement(): Unit = this.synchronized {
      println("Thread Id: " + Thread.currentThread.getId +" started")
      Thread.sleep(7700)
      println("Thread Id: " + Thread.currentThread.getId +" finished")
    }

    val threadPool = List.tabulate(4){ _ =>
      new Thread(() => {
        workerIncrement()
      })
    }

    threadPool.map(_.start())
    threadPool.map(_.join())

    // OUTPUT:
    // this Thread id before waiting: 155
    // this Thread id after waiting: 155
    // this Thread id before waiting: 159
    // this Thread id after waiting: 159
    // this Thread id before waiting: 158
    // this Thread id after waiting: 158
    // this Thread id before waiting: 157
    // this Thread id after waiting: 157
    // this Thread id before waiting: 156
    // this Thread id after waiting: 156
  }
}
