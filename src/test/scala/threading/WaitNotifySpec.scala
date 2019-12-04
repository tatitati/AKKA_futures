package threading

import org.scalatest.FunSuite

class WaitNotifySpec extends FunSuite {

  test("wait + notify: this can be used to introduce data every X items"){
    val lock = new AnyRef
    var msg: String = ""

    val t1 = new Thread {
      override def run(): Unit = {
        lock.synchronized{
          for(_ <- 1 to 3){
            msg = msg + "a"
            println(msg)
          }
          println("wait")
          lock.wait()
          println("I was notified!")
          for(_ <- 1 to 3){
            msg = msg + "b"
            println(msg)
          }
        }
      }
    }

    t1.start()
    Thread.sleep(2000)
    lock.synchronized {
      for(_ <- 1 to 3) {
        msg = msg + "c"
        println(msg)
      }
      lock.notify()
    }

    t1.join()

    // OUTPUT
    //
    // a
    // aa
    // aaa
    // wait
    // aaac
    // aaacc
    // aaaccc
    // I was notified!
    // aaacccb
    // aaacccbb
    // aaacccbbb
  }

  test("wait + notify: this can be used to rely in another thread to do something when the item is ready"){
    val lock = new AnyRef
    var msg: String = ""

    val t1 = new Thread {
      override def run(): Unit = {
        lock.synchronized{
          while(msg.length < 9){
            println("wait")
            lock.wait()
          }
          println("I was notified!")
          println(msg)
        }
      }
    }

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


