package lazyevaluation

object Exercises extends App {
  // EXERCISE:
  // implemente a lazily evaluated, singly linked STREAM of elements

  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepent operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // contaenate two streams

    def foreachf(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    // example: naturals.take(100)  is lazily evaluated stream of the first 100 naturals (finite stream)
    // naturals.take(100).foreach(println) ===> OK
    // naturals.foreach(println) =====> will crash, is infinite
    def take(n: Int):MyStream[A]
    def takeAsList(n: Int):List[A]
  }

  object MyStream {
    // example: MyStream.from(1)(x => x + 1)   is an stream of natural numbers (infinite)
    def from[A](start: A)(generator: A => A): MyStream[A] = ???
  }
}
