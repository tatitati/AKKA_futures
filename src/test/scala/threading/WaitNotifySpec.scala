package threading

import org.scalatest.FunSuite

class WaitNotifySpec extends FunSuite {

  test("wait + notify"){
    val lock = new AnyRef
    var msg: String = ""

    val t1 = new Thread {
      override def run(): Unit = {
        lock.synchronized{
          for(_ <- 1 to 9){
            msg = msg + "a"
            println(msg)
          }
          println("wait")
          lock.wait()
          println("I was notified!")
          for(_ <- 1 to 9){
            msg = msg + "b"
            println(msg)
          }
        }
      }
    }

    t1.start()
    Thread.sleep(10000)
    lock.synchronized {
      for(_ <- 1 to 9) {
        msg = msg + "c"
        println(msg)
      }
      lock.notify()
    }

    t1.join()
    println(msg)
  }
}

// OUTPUT
//
// a
// aa
// aaa
// aaaa
// aaaaa
// aaaaaa
// aaaaaaa
// aaaaaaaa
// aaaaaaaaa
// wait
// aaaaaaaaac
// aaaaaaaaacc
// aaaaaaaaaccc
// aaaaaaaaacccc
// aaaaaaaaaccccc
// aaaaaaaaacccccc
// aaaaaaaaaccccccc
// aaaaaaaaacccccccc
// aaaaaaaaaccccccccc
// I was notified!
// aaaaaaaaacccccccccb
// aaaaaaaaacccccccccbb
// aaaaaaaaacccccccccbbb
// aaaaaaaaacccccccccbbbb
// aaaaaaaaacccccccccbbbbb
// aaaaaaaaacccccccccbbbbbb
// aaaaaaaaacccccccccbbbbbbb
// aaaaaaaaacccccccccbbbbbbbb
// aaaaaaaaacccccccccbbbbbbbbb
// aaaaaaaaacccccccccbbbbbbbbb
