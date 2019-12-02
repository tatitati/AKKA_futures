package threading

import org.scalatest.FunSuite

class SynchronizedSpec extends FunSuite {
  test("Dead race scenario") {
    var num = 0
    def increment(): Int = {
      for (_ <- 1 to 100000) {
        num = num + 1
      }
      num
    }

    val t1 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t2 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t3 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t4 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t5 = new Thread{
      override def run(): Unit = {increment()}
    }

    val threads = List(t1, t2, t3, t4, t5)
    threads.map(_.start())
    threads.map(_.join())

    println(num)

    // OUTPUT:
    // 115789
    // 15885
    // 13383
  }

  test("Synchronization with Lock scenario") {
    var num = 0
    def increment(): Int = {
      this.synchronized{ // we changed only this line
        //println("this is my FIRST thread :: "+ "Thread id: " + Thread.currentThread.getId)
        //Thread.sleep(2000) // is it possible to put even an sleep, until this block doesnt finish no other will ocuppy this code
        for (_ <- 1 to 100000) {
          num = num + 1
        }
        num
      }
    }

    val t1 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t2 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t3 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t4 = new Thread{
      override def run(): Unit = {increment()}
    }
    val t5 = new Thread{
      override def run(): Unit = {increment()}
    }

    val threads = List(t1, t2, t3, t4, t5)
    threads.map(_.start())
    threads.map(_.join())

    println(num)

    // OUTPUT:
    // 500000
    // 500000
    // 500000
  }
}
