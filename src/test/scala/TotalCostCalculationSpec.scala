import com.phone.io.{LogReader, RecordReader}
import com.phone.program.CalculateCosts.CostPerCustomer
import com.phone.program.{CalculateCosts, CallCost, Promotion}
import org.scalatest.{AsyncWordSpec, Matchers}

class TotalCostCalculationSpec extends AsyncWordSpec with Matchers {
  "TotalCosts" should {
    "Be able to successfully calculate the total costs with promotion" in {
      CalculateCosts
        .calculateCosts(getClass.getResource("calls.log").getPath)(
          LogReader.FileLogReader,
          RecordReader.FileRecordReader,
          Promotion.DeductGreatestCostCallToPhoneNumber,
          CallCost.FivePThenThreeP
        )
        .unsafeToFuture
        .map(
          _.fold(
            errors =>
              fail(s"Errors where thrown ${errors
                .foldLeft("")((acc, error) => s"${error.toString},$acc")}"),
            _ should equal(
              List(
                CostPerCustomer("A", BigDecimal(31.38)),
                CostPerCustomer("B", BigDecimal(30.08))
              )
            )
          )
        )
    }
  }
}
