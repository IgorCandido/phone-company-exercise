package com.phone.io

import com.phone.models.Call
import com.phone.time.TimeHelper
import cats.implicits._
import com.phone.errors.PhoneError.{FailedToParseCall, Validated}

trait RecordReader {
  def readRecords(logLines: Seq[String]): Validated[Seq[Call]]
}

object RecordReader {
  implicit object FileRecordReader extends RecordReader {
    private val CallRegex = "(.*) (.*) (.*)".r

    private def parseCall(callLog: String): Validated[Call] = {
      callLog match {
        case CallRegex(customerId, phoneNumber, duration) =>
          TimeHelper.parseTimeToSeconds(duration).map{
            Call(customerId, phoneNumber, _)
          }
        case _ =>
          FailedToParseCall.invalidNec

      }
    }

    override def readRecords(logLines: Seq[String]): Validated[Seq[Call]] = {
      (for {
        line <- logLines
        call = parseCall(line)
      } yield call).toList.traverse(identity)
    }
  }
}
