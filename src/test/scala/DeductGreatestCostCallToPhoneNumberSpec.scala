import com.phone.models.Call
import com.phone.program.Promotion.DeductGreatestCostCallToPhoneNumber
import org.scalatest.{Matchers, WordSpec}

class DeductGreatestCostCallToPhoneNumberSpec extends WordSpec with Matchers {
  "DeductGreatestCostCallToPhoneNumber" should {
    case class DeductCostTestCase(testName: String,
                                  calls: Seq[Call],
                                  cost: BigDecimal)
    List(
      DeductCostTestCase(
        "remove the costs of calling the phoneNumber with more costs",
        Seq(
          Call("A", "555-333-212", 2),
          Call("A", "555-333-212", 4),
          Call("A", "555-333-211", 2)
        ),
        4
      ),
      DeductCostTestCase(
        "remove all costs if only one number called",
        Seq(
          Call("A", "555-333-212", 2),
          Call("A", "555-333-212", 4),
          Call("A", "555-333-212", 2)
        ),
        0
      ),
      DeductCostTestCase("No costs if no calls", Seq.empty, 0)
    ).foreach { deductCostTestCase =>
      deductCostTestCase.testName in {
        val cost = DeductGreatestCostCallToPhoneNumber.CostWithPromotion(
          deductCostTestCase.calls
        )(call => call.callDuration * 2)

        cost should equal(deductCostTestCase.cost)
      }
    }
  }
}
