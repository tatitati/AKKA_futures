package threading

import org.scalatest.FunSuite
import scala.collection.mutable

class ProducerConsumerNotifyingBufferSpec extends FunSuite {

  def producerConsumer() = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      while(true) {
        buffer.synchronized{
          if(buffer.isEmpty){
            println("\t[consumer] buffer empty, waiting...");buffer.wait()
          }

          println("\t[consumer] consumed " + buffer.dequeue())
          buffer.notify()
        }
      }
    })

    val producer = new Thread(() => {
      var i = 0

      while(true) {
        buffer.synchronized {
          if(buffer.size == capacity) {
            println("[producer] buffer is full, waiting....");buffer.wait()
          }


          buffer.enqueue(i); println("[producer] produced " + i)
          buffer.notify()

          i += 1
        }
      }
    })

    consumer.start();producer.start()
    consumer.join();producer.join()
  }

  test("run") {
    producerConsumer()

    // [producer] produced3
    // [producer] produced4
    // [producer] produced5
    // [producer] buffer is full, waiting....
    //   [consumer] consumed 3
    //   [consumer] consumed 4
    //   [consumer] consumed 5
    //   [consumer] buffer empty, waiting...
    // [producer] produced6
    // [producer] produced7
    // [producer] produced8

  }
}
