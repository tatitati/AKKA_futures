package lazyevaluation

object Lazyexamples extends App {

  def exampleCallByName() = {
    println("EXAMPLE 1:")

    def byNameMethod(fn: => Int): Int = {
      // NOTE: call by name is evaluated three times!
      fn + fn + fn + 1
    }

    def retrieveMagicValue(): Int = {
      println("waiting....")
      42
    }

    byNameMethod(retrieveMagicValue)
    // >
    // waiting....
    // waiting....
    // waiting....
  }

  exampleCallByName()


  def exampleCallByNeed() = {
    println("EXAMPLE 2:")

    def byNameMethod(fn: => Int): Int = {
      // NOTE: now is evaluated only 1.
      //       This technique is named "CALL BY NEED"
      lazy val lv = fn
      lv + lv + lv + 1
    }

    def retrieveMagicValue(): Int = {
      println("waiting....")
      42
    }

    byNameMethod(retrieveMagicValue)
    // >
    // waiting....
  }

  exampleCallByNeed()

  def exampleFiltering(): Unit = {
    println("EXAMPLE 3:")

    def lesstThan30(i: Int): Boolean = {
      println(s"LESS THAN 30?: $i")
      i < 30
    }

    def greaterThan20(i: Int): Boolean = {
      println(s"GREATER THAN 20?: $i")
      i > 20
    }

    val numbers = List(1, 25, 40, 5, 23)
    val result = numbers
      .filter(lesstThan30)    // > List(1, 25, 5, 23)
      .filter(greaterThan20)
    println(result)
    // >
    // LESS THAN 30?: 1
    // LESS THAN 30?: 25
    // LESS THAN 30?: 40
    // LESS THAN 30?: 5
    // LESS THAN 30?: 23
    // GREATER THAN 20?: 1
    // GREATER THAN 20?: 25
    // GREATER THAN 20?: 5
    // GREATER THAN 20?: 23
    // List(25, 23)
  }

  exampleFiltering()

  def exampleFilteringLazy(): Unit = {
    println("EXAMPLE 4:")

    def lesstThan30(i: Int): Boolean = {
      println(s"LESS THAN 30?: $i")
      i < 30
    }

    def greaterThan20(i: Int): Boolean = {
      println(s"GREATER THAN 20?: $i")
      i > 20
    }

    val numbers = List(1, 25, 40, 5, 23)
    val result = numbers
      .withFilter(lesstThan30)
      .withFilter(greaterThan20) // withFilter uses lazy vals

    // the filtering never takes place, is lazy
    println(result)
    // >
    // scala.collection.TraversableLike$WithFilter@668bc3d5

    // now i force the evaluation, as you can see it behaves more like an stream,
    // in which each stage only acts when it receives data (the stage is lazy, it
    // works on deman)
    result.foreach(println)
    // >
    // LESS THAN 30?: 1
    // GREATER THAN 20?: 1
    // LESS THAN 30?: 25
    // GREATER THAN 20?: 25
    // 25
    // LESS THAN 30?: 40
    // LESS THAN 30?: 5
    // GREATER THAN 20?: 5
    // LESS THAN 30?: 23
    // GREATER THAN 20?: 23
    // 23

  }

  exampleFilteringLazy()


  def exampleForcomprehension(): Unit ={
    println("EXAMPLE 5")

    // for comprehensions use .withFilter under the hood
    val resultFor = for{
      a <- List(1,2,3) if a % 2 == 0
    } yield a + 1

    val resultWithfilter = List(1,2,3)
      .withFilter(_ % 2 == 0)
      .map(_ + 1)

    println(resultFor)        // > List(3)
    println(resultWithfilter) // > List(3)
  }

  exampleForcomprehension()
}
