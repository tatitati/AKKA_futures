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

  test("In this way I don't need to start() the thread, it starts automatically"){
    val t = new Thread {
      println("this is my SECOND thread :: "+ "Thread id: " + Thread.currentThread.getId)
    }

    t.join()
    println("done")
  }
}

//  OUTPUT:
//  =======
//  this is my FIRST thread
//  done
//  this is my SECOND thread
//  done
