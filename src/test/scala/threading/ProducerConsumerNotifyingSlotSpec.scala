package threading

import org.scalatest.FunSuite

class ProducerConsumerNotifyingSlotSpec extends FunSuite {

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
        println("\t[consumer] waiting..."); container.wait()
      }

      println("\t[consumer] I consumed " + container.get)
    })


    val producer = new Thread(() => {
      println("[producer] working...")

      container.synchronized {
        container.set(42); println("[producer] I produced 42")
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
