package com.phone.program

import cats.Show
import cats.effect.IO
import com.phone.errors.PhoneError.Validated
import com.phone.io.{LogReader, RecordReader}
import com.phone.models.Call

object CalculateCosts {
  case class CostPerCustomer(customerId: String, cost: BigDecimal)

  object CostPerCustomer {
    implicit val showCall = Show.show[CostPerCustomer] { costPerCustomer =>
      s"${costPerCustomer.customerId} ${costPerCustomer.cost} $$"
    }
  }

  private def calculateCostsPerCustomer(calls: Validated[Seq[Call]])(
    implicit
    promotion: Promotion,
    callCost: CallCost
  ): Validated[Seq[CostPerCustomer]] =
    calls.map(_.groupBy(_.customerId).toList.map {
      case (customerId, calls) =>
        CostPerCustomer(
          customerId,
          promotion.CostWithPromotion(calls)(callCost.calculate)
        )
    })

  def calculateCosts(fileLocation: String)(
    implicit reader: LogReader[IO],
    recordReader: RecordReader,
    promotion: Promotion,
    callCost: CallCost
  ): IO[Validated[Seq[CostPerCustomer]]] =
    for {
      lines <- reader.read(fileLocation)
      callsOrInvalid <- IO.pure(recordReader.readRecords(lines))
      res <- IO.pure(calculateCostsPerCustomer(callsOrInvalid))
    } yield res

}
