package threading

import org.scalatest.FunSuite

import scala.collection.mutable
import scala.util.Random

class ProducerConsumerBufferSpec extends FunSuite {

  def producerConsumer() = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumer = new Thread(() => {
      val random = new Random()

      while(true) {
        buffer.synchronized{
          if(buffer.isEmpty){
            println("\t[consumer] buffer empty, waiting...")
            buffer.wait()
          }

          println("\t[consumer] consumed " + buffer.dequeue())
          buffer.notify()
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    val producer = new Thread(() => {
      val random = new Random()
      var i = 0

      while(true) {
        buffer.synchronized {
          if(buffer.size == capacity) {
            println("[producer] buffer is full, waiting....")
            buffer.wait()
          }

          println("[producer] producing" + i)
          buffer.enqueue(i)
          buffer.notify()

          i += 1
        }

        Thread.sleep(random.nextInt(500))
      }
    })

    consumer.start();consumer.join()
    producer.start();producer.join()
  }

  test("run") {
    producerConsumer()
  }
}
