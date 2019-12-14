package threading

import org.scalatest.FunSuite

class AnyrefLockSpec extends FunSuite {

  test("Is interesting that I take the lock of a different variable to the one I'm modifying....seems that I don't need to lock the variable that is going to be modified"){
    val lock = new AnyRef
    var sharedMsg: String = ""

    val t1 = new Thread (() => {
      lock.synchronized{
        for(_ <- 1 to 3){
          sharedMsg = sharedMsg + "1"
          println("[t1] " + sharedMsg)
        }
        println("[t1] waiting..")
        lock.wait()
        println("[t1] notified")

        for(_ <- 4 to 6){
          sharedMsg = sharedMsg + "1"
          println("[t1] " + sharedMsg)
        }
      }
    })

    val t2 = new Thread(() => {
      lock.synchronized {
        for(_ <- 1 to 3) {
          sharedMsg = sharedMsg + "2"
          println("\t[t2] " + sharedMsg)
        }
        lock.notify()
      }
    })
    
    t1.start(); t2.start()
    t1.join(); t2.join()
  }
}


