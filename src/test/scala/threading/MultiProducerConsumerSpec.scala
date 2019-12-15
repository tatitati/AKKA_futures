package threading

import org.scalatest.FunSuite
import scala.collection.mutable
import scala.util.Random

class MultiProducerConsumerSpec extends FunSuite {

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread(() => {
    while(true) {
      buffer.synchronized {
        while(buffer.isEmpty){ // why while and not if!!!!!??!?!?!?!?!
          println("\t[consumer] buffer empty, waiting...")
          buffer.wait()
        }

        val x = buffer.dequeue()
        println(s"\t[consumer] consumed $x")
        buffer.notify()
      }
    }
  })

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread(() => {
    var i = 0

    while(true){
      buffer.synchronized{
        while(buffer.size == capacity){ // why while and not if??????
          println("[producer] buffer is full, waiting..")
          buffer.wait()
        }

        println(s"[producer] producing $i")
        buffer.enqueue(i)

        buffer.notify()

        i += 1
      }
    }
  })

  def multipProducerConsumer(nConsumers: Int, nProducers: Int) = {
    val buffer: mutable.Queue[Int] = new mutable.Queue[Int]
    val capacity = 3

    val consumers = List.tabulate(nConsumers)( i =>
      new Consumer(i, buffer)
    )

    val producers = List.tabulate(nProducers)(i =>
      new Producer(i, buffer, capacity)
    )

    consumers.map(_.start()); producers.map(_.start());
    consumers.map(_.join()); producers.map(_.join);
  }

  test("run") {
    multipProducerConsumer(3, 3)
  }

}
