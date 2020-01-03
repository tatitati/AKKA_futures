package lazyevaluation

object Lazyexamples extends App {

  def example1() = {
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

  example1()


  def example2() = {
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
    // waiting....
    // waiting....
  }

  example2()
}
