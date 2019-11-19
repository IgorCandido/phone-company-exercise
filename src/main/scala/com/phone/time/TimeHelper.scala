package com.phone.time

import java.text.SimpleDateFormat

import cats.implicits._
import com.phone.errors.PhoneError.{FailedToParseTime, Validated}
import org.joda.time.{DateTime, Duration}

import scala.util.Try

object TimeHelper {
  def parseTimeToSeconds(time: String): Validated[Long] = {
    Try {
      new DateTime(new SimpleDateFormat("HH:mm:ss").parse(time).getTime())
    }.fold(throwable => FailedToParseTime(throwable).invalidNec, _.validNec)
      .map { fullDatTime =>
        val fromMidnight = fullDatTime.toDateTime.withTimeAtStartOfDay()
        val duration = new Duration(fromMidnight, fullDatTime)
        duration.toStandardSeconds().getSeconds()
      }
  }
}
