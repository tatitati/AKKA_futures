package threading

import org.scalatest.FunSuite
import scala.collection.mutable

class ProducerConsumerBufferSpec extends FunSuite {

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


          buffer.enqueue(i); println("[producer] produced" + i)
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

    //[producer] buffer is full, waiting....
    //  [consumer] consumed 149
    //[producer] producing152
    //  [consumer] consumed 150
    //[producer] producing153
    //  [consumer] consumed 151
    //[producer] producing154
    //  [consumer] consumed 152
    //[producer] producing155
    //  [consumer] consumed 153
  }
}
