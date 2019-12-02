package threading

import org.scalatest.FunSuite

class SimpleThreadSpec extends FunSuite {
  test("can create a very basic thread"){
    class MyThread extends Thread {
      override def run(): Unit = {
        println("this is my FIRST thread")
      }
    }

    val t = new MyThread()
    t.start()
    t.join()
    println("done")
  }

  test("Another way"){
    val t = new Thread {
      override def run(): Unit = {
        println("this is my SECOND thread")
      }
    }

    t.start()
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
