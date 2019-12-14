package threading

import org.scalatest.FunSuite

class Synchronized2Spec extends FunSuite {
  test("A Thread.sleep inside of a synchronized doesnt produce a change to another thread becauase it owns the lock still") {

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
