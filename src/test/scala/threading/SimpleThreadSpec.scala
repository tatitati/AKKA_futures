package threading

import org.scalatest.FunSuite

class SimpleThreadSpec extends FunSuite {

  test("I can create a very basic thread"){
    val t = new Thread {
      override def run(): Unit = {
        println("this is my FIRST thread :: "+ "Thread id: " + Thread.currentThread.getId)
      }
    }

    t.start()
    t.join()
    println("done")
  }
}

//  OUTPUT:
//  =======
// this is my FIRST thread :: Thread id: 160
// done

