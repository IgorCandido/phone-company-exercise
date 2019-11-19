package com.phone.program

import cats.Monoid
import cats.implicits._
import com.phone.models.Call

trait Promotion {
  def CostWithPromotion(calls: Seq[Call])(
    normalCostOfACall: Call => BigDecimal
  ): BigDecimal
}

object Promotion {
  private case class CostPerPhoneNumber(phoneNumber: String, cost: BigDecimal)

  object DeductGreatestCostCallToPhoneNumber extends Promotion {
    override def CostWithPromotion(
      calls: Seq[Call]
    )(normalCostOfACall: Call => BigDecimal): BigDecimal =
      Monoid.combineAll {
        calls
          .map(
            call =>
              CostPerPhoneNumber(call.phoneNumber, normalCostOfACall(call))
          )
          .groupBy(_.phoneNumber)
          .map[BigDecimal] {
            case (_, costPerPhoneNumberSeq) =>
              costPerPhoneNumberSeq.foldLeft(BigDecimal(0))(
                (acc, costPerPhoneNumber) => costPerPhoneNumber.cost + acc
              )

          }
          .toSeq
          .sortBy { costPerPhoneNumber =>
            -costPerPhoneNumber
          }
          .drop(1)
      }
  }
}
