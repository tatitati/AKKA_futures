package lazyevaluation

import scala.annotation.tailrec

object Exercises extends App {
  // EXERCISE:
  // implemente a lazily evaluated, singly linked STREAM of elements

  // example: MyStream.from(1)(x => x + 1)   is an stream of natural numbers (infinite)
  // example: naturals.take(100)  is lazily evaluated stream of the first 100 naturals (finite stream)
  // naturals.take(100).foreach(println) ===> OK
  // naturals.foreach(println) =====> will crash, might be infinite
  // naturals.map(_ * 2) =====> will crash, might be infinite
  abstract class MyStream[+A] {
    def isEmpty: Boolean
    def head: A
    def tail: MyStream[A]

    def #::[B >: A](element: B): MyStream[B] // prepend operator
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] // contatenate two streams

    def foreach(f: A => Unit): Unit
    def map[B](f: A => B): MyStream[B]
    def flatMap[B](f: A => MyStream[B]): MyStream[B]
    def filter(predicate: A => Boolean): MyStream[A]

    def take(n: Int):MyStream[A]
    def takeAsList(n: Int):List[A] = take(n).toList()

    /**
     * [1,2,3].toList([])
     * [2,3].toList([1])
     * [3].toList([2,1])
     * [].toList([3,2,1])
     * [1,2,3]
     */
    @tailrec
    final def toList[B >: A](acc: List[B] = Nil): List[B] =
      if (isEmpty) acc.reverse
      else tail.toList(head :: acc)
  }

  object EmptyMyStream extends MyStream[Nothing] {
    def isEmpty: Boolean = true
    def head: Nothing = throw new NoSuchElementException
    def tail: MyStream[Nothing] = throw new NoSuchElementException

    def #::[B >: Nothing](element: B): MyStream[B] = new Cons(element, this)
    def ++[B >: Nothing](anotherStream: MyStream[B]): MyStream[B] = anotherStream

    def foreach(f: Nothing => Unit): Unit = ()
    def map[B](f: Nothing => B): MyStream[B] = this
    def flatMap[B](f: Nothing => MyStream[B]): MyStream[B] = this
    def filter(predicate: Nothing => Boolean): MyStream[Nothing] = this

    def take(n: Int):MyStream[Nothing] = this
  }

  class Cons[+A](hd: A, tl: => MyStream[A]) extends MyStream[A] {
    def isEmpty: Boolean  = false
    override val head: A = hd
    // important: call by need
    override lazy val tail: MyStream[A] = tl

    def #::[B >: A](element: B): MyStream[B] = new Cons(element, this)
    def ++[B >: A](anotherStream: MyStream[B]): MyStream[B] = new Cons(head, tail ++ anotherStream)

    def foreach(f: A => Unit): Unit = {
      f(head)
      tail.foreach(f)
    }
    def map[B](f: A => B): MyStream[B] = new Cons(f(head), tail.map(f))
    def flatMap[B](f: A => MyStream[B]): MyStream[B] = f(head) ++ tail.flatMap(f)
    def filter(predicate: A => Boolean): MyStream[A] =
      if(predicate(head)) new Cons(head, tail.filter(predicate))
      else tail.filter(predicate)

    def take(n: Int):MyStream[A] =
      if(n <= 0) EmptyMyStream
      else if (n == 1) new Cons(head, EmptyMyStream)
      else new Cons(head, tail.take(n-1))
  }

  object MyStream {
    def from[A](start: A)(generator: A => A): MyStream[A] =
      new Cons(start, MyStream.from(generator(start))(generator))
  }

  val naturals = MyStream.from(1)(_ + 1)
  println(naturals.head) //> 1
  println(naturals.tail) //> lazyevaluation.Exercises$Cons@511baa65
  println(naturals.tail.head) //> 2
  println(naturals.tail.tail.head) //> 3
}
