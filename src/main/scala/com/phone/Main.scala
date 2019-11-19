package com.phone

import cats.effect.IO
import cats.implicits._
import com.phone.helper.ShowHelper._
import com.phone.io.{LogReader, RecordReader, Console}
import com.phone.program.{CalculateCosts, CallCost, Promotion}


object Main extends App {
  (for {
    path <- IO { getClass.getResource("/calls.log").getPath }
    costs <- CalculateCosts
      .calculateCosts(path)(
        LogReader.FileLogReader,
        RecordReader.FileRecordReader,
        Promotion.DeductGreatestCostCallToPhoneNumber,
        CallCost.FivePThenThreeP
      )
    str <- IO {
      costs.fold(
        errors =>
          s"Errors where thrown ${errors
            .foldLeft("")((acc, error) => s"${error.toString},$acc")}",
        a => a.show
      )
    }
    _ <- Console[IO].print(str)
  } yield ()).unsafeRunSync()
}
