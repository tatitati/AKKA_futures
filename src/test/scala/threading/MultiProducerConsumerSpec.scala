package threading

import org.scalatest.FunSuite
import scala.collection.mutable
import scala.util.Random

class MultiProducerConsumerSpec extends FunSuite {

  class Consumer(id: Int, buffer: mutable.Queue[Int]) extends Thread(() => {
    while(true) {
      buffer.synchronized {
        while(buffer.isEmpty){ // why while and not if!!!!!??!?!?!?!?!
          println(s"\t[consumer-$id] buffer empty, waiting...")
          buffer.wait()
        }

        val x = buffer.dequeue()
        println(s"\t[consumer-$id] consumed $x")
        buffer.notify()
      }
    }
  })

  class Producer(id: Int, buffer: mutable.Queue[Int], capacity: Int) extends Thread(() => {
    var i = 0

    while(true){
      buffer.synchronized{
        while(buffer.size == capacity){ // why while and not if??????
          println(s"[producer-$id] buffer is full, waiting..")
          buffer.wait()
        }

        println(s"[producer-$id] producing $i")
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
