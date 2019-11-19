package com.phone.errors
import cats.data._

sealed trait PhoneError extends Throwable

object PhoneError {
  type Validated[A] = ValidatedNec[PhoneError,A]

  final case object FailedToParseCall extends PhoneError
  final case class FailedToParseTime(throwable: Throwable) extends PhoneError
}
