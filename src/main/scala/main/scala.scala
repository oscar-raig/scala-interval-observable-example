package main

import rx.lang.scala.Observable
import rx.lang.scala.Subscriber

import scala.concurrent.duration._

class scala {

  def NUMBER_ITEMS = 10

  def main(args: Array[String]) {
    val lock: AnyRef = new AnyRef
    val a = Observable.interval(1000 millis).map(_ => 1)
    val d = Observable.interval(3000 millis).map(_ => 3)
    val e = Observable.interval(5000 millis).map(_ => 5)
    a.merge(d).merge(e)
      .take(NUMBER_ITEMS)
      .subscribe(new Subscriber[Int]() {
        override def onCompleted() {
            lock synchronized {
              // I have finished, signalling lock
              lock.notifyAll()
          }
        }
        override def onError(throwable: Throwable) {}
        override def onNext(s: Int) = println(s)
      })
    lock synchronized {
      //waiting to subscriber To Finish
      lock.wait()
    }
  }
}

