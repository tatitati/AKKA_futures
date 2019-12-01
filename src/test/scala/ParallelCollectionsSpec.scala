import org.scalatest.FunSuite

import scala.collection.parallel.immutable.ParVector

class ParallelCollectionsSpec extends FunSuite {
  test("parallel collections dont follow any order"){
    val numbers = List(1, 2, 3, 4, 5, 6, 7, 8)

    //    val result = numbers.par.foreach{ println }
    //    3
    //    2
    //    4
    //    1
    //    5
    //    6
    //    7
    //    8
    //    ()
  }

  test("par.map keeps the order as well") {
    val numbers = List(1, 2, 3, 4, 5, 6, 7, 8)
    val result = numbers.par.map{ x =>
       println(Thread.currentThread().getName() + " is picking up the number: " + x)
      val r = scala.util.Random
      Thread.sleep(r.nextInt(5000))
      x * 10
    }
    assert(ParVector(10, 20, 30, 40, 50, 60, 70, 80) == result)
  }
}
