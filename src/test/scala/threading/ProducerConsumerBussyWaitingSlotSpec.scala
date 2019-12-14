package threading

import org.scalatest.FunSuite

class ProducerConsumerBussyWaitingSlotSpec extends FunSuite {

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

  def producerConsumer(): Unit = {
    val container = new Container
    val consumer = new Thread(() => {
      while(container.isEmpty){
        println("\t[consumer] actively waiting...")
      }

      println("\t[consumer] I have consummed" + container.get)
    })

    val producer = new Thread(() => {
      println("[producer] computing...")
      Thread.sleep(500)
      container.set(42)
      println("[producer] I have produced after long work the value: 42")
    })

    consumer.start();producer.start()
    consumer.join();producer.join()
  }

  test("run"){
    producerConsumer()

    // OUTPUT
    // ========================================
    //    [consumer] actively waiting...
    //    [consumer] actively waiting...
    //    [consumer] actively waiting...
    //    [consumer] actively waiting...
    //    [consumer] actively waiting...
    //[producer] I have produced after long work the value: 42
    //    [consumer] I have consummed42

  }
}
