package threading

import org.scalatest.FunSuite

class ProducerConsumerNotifyingSpec extends FunSuite {

  class Container {
    private var value: Int = 0

    def isEmpty: Boolean = value == 0
    def set(newValue: Int) = value = newValue
    def get = {
      val result = value
      value = 0
      result
    }
  }

  def smartProducerConsumer() = {
    val container = new Container()

    val consumer = new Thread(() => {
      container.synchronized{
        container.wait()
      }

      println("\t[consumer] I have consumed " + container.get)
    })


    val producer = new Thread(() => {
      println("[producer] working...")
      Thread.sleep(2000)
      val value = 42

      container.synchronized {
        println("[producer] I'm producing " + value)
        container.set(value)
        container.notify()
      }
    })

    consumer.start()
    producer.start()
    consumer.join()
    producer.join()
  }

  test("run"){
      smartProducerConsumer()
  }
}
