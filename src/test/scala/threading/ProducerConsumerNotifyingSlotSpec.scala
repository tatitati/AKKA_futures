package threading

import org.scalatest.FunSuite

class ProducerConsumerNotifyingSlotSpec extends FunSuite {

  class Slot {
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
    val slot = new Slot()

    val consumer = new Thread(() => {
      slot.synchronized{
        println("\t[consumer] waiting..."); slot.wait()
      }

      println("\t[consumer] I consumed " + slot.get)
    })


    val producer = new Thread(() => {
      println("[producer] working...")

      slot.synchronized {
        slot.set(42); println("[producer] I produced 42")
        slot.notify()
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
