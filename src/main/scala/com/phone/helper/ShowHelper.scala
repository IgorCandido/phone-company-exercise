package com.phone.helper

import cats.Show
import cats.implicits._

object ShowHelper {
  implicit def showSeq[A](implicit show: Show[A]) =
    Show.show[Seq[A]]{ _.foldLeft(""){(acc, a) => show"$a \n$acc"}}
}
