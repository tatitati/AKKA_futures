package threading

import org.scalatest.FunSuite
import scala.collection.mutable.ListBuffer

class WaitNotify3Spec extends FunSuite {

  test("consumer-producer"){
    var queue = new ListBuffer[String]()
    val lock = new AnyRef
    var existNewItem = false;

    val consumer = new Thread{
        override def run()= while(true) {
          lock.synchronized{
            println("CONSUMER LOCK")
            while (!existNewItem) {
              println("waiting...")
              lock.wait
            }
            println("\tNew item: " + queue.last)
            existNewItem = false
          }
        }
    }

    val producer = new Thread{
      override def run() = while(true){
        lock.synchronized{
          println("PRODUCER LOCK")
          queue += "whatever1"
          existNewItem = true
          Thread.sleep(5000)
          lock.notify()
        }
      }
    }

    val threadPool = List(producer, consumer)
    threadPool.map(_.start())
    threadPool.map(_.join())

  }
}
