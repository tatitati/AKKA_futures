package threading

import org.scalatest.FunSuite
import scala.collection.mutable.ListBuffer

class WaitNotify3Spec extends FunSuite {

  test("consumer-producer"){
    var queue = new ListBuffer[String]()
    val lock = new AnyRef
    var existNewItem = false;

    val consumer = new Thread(() => {
      while(true) {
        lock.synchronized{
          println("[consumer] LOCK")
          while (!existNewItem) {
            println("[consumer] waiting...")
            lock.wait
          }
          println("\t[consumer] New item: " + queue.last)
          existNewItem = false
        }
      }
    })

    val producer = new Thread(() => {
      while(true){
        lock.synchronized{
          println("[producer] LOCK")
          queue += "whatever1"
          existNewItem = true
          Thread.sleep(5000)
          lock.notify()
        }
      }
    })

    val threadPool = List(producer, consumer)
    threadPool.map(_.start())
    threadPool.map(_.join())

  }
}
