package com.phone.program

import com.phone.models.Call

trait CallCost {
  def calculate(call: Call): BigDecimal
}

object CallCost {
  object FivePThenThreeP extends CallCost {
    private val priceFirstThreeMinutes = BigDecimal(0.05)
    private val pricePostThreeMinutes = BigDecimal(0.03)
    private val threeMinutes = 3 * 60

    override def calculate(call: Call): BigDecimal =
      call.callDuration match {
        case duration if duration <= threeMinutes =>
          duration * priceFirstThreeMinutes
        case duration =>
          ((duration - threeMinutes) * pricePostThreeMinutes) + (threeMinutes * priceFirstThreeMinutes)
      }
  }
}
