package threading

import org.scalatest.FunSuite

class WaitNotify2Spec extends FunSuite {

  test("Thread guard:: wait + notify: this can be used to rely in another thread to do something when the item is ready"){
    val lock = new AnyRef
    var msg: String = ""

    val t1 = new Thread (() => {
      lock.synchronized {
        while (msg.length < 9) {
          println("wait")
          lock.wait()
        }
        println("I was notified!")
        println(msg)
      }
    })

    t1.start()
    lock.synchronized {
      msg = msg + "c"
      msg = msg + "casdf"
      msg = msg + "casdfasdfasdf"
      println("Notifying...")
      lock.notify()
    }

    t1.join()

    // OUTPUT:
    // =======
    // Notifying...
    // I was notified!
    // ccasdfcasdfasdfasdf
  }
}


