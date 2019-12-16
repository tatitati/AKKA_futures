package threading

import org.scalatest.FunSuite

class NotifyAllSpec extends FunSuite {

  def runBells() = {
    val bell = new Object()

    val waiters = List.tabulate(5){ i =>
      new Thread(() => {
        bell.synchronized {
          println(s"\t[Thread $i] waiting...")
          bell.wait()
          println(s"\t[Thread $i] resumed...")
        }
      })
    }

    val activator = new Thread(() => {
      println("[Main] activating ....")
      Thread.sleep(2000)
      bell.synchronized{
        bell.notifyAll()
      }
    })

    waiters.map(_.start()); activator.start()
    waiters.map(_.join()); activator.join()
  }

  test("run"){
    runBells()

    // Output with NotifyAll()
    // ======================
    //  [Thread 0] waiting...
    //  [Thread 1] waiting...
    //  [Thread 2] waiting...
    //  [Thread 3] waiting...
    //  [Thread 4] waiting...
    // [Main] activating ....
    //  [Thread 4] resumed...
    //  [Thread 3] resumed...
    //  [Thread 2] resumed...
    //  [Thread 1] resumed...
    //  [Thread 0] resumed...

  }
}
