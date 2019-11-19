import com.phone.models.Call
import com.phone.program.CallCost.FivePThenThreeP
import org.scalatest.{Matchers, WordSpec}

class FivePThanThreePCostSpec extends WordSpec with Matchers {
  "FivePThanThreePCost" should {
    case class FivePThanThreePTestCase(testName: String,
                                       calls: Seq[Call],
                                       cost: BigDecimal)

    List(
      FivePThanThreePTestCase(
        "1 calls of 3 minutes",
        Seq(
          Call("A", "555-433-242", 3 * 60),
        ),
        BigDecimal(9)
      ),
      FivePThanThreePTestCase(
        "3 calls of 3 minutes",
        Seq(
          Call("A", "555-433-242", 3 * 60),
          Call("A", "555-433-242", 3 * 60),
          Call("A", "555-433-242", 3 * 60)
        ),
        BigDecimal(27)
      ),
      FivePThanThreePTestCase(
        "1 call of 5 minutes",
        Seq(
          Call("A", "555-433-242", 5 * 60)
        ),
        BigDecimal(12.6)
      ),
      FivePThanThreePTestCase(
        "2 call of 5 minutes",
        Seq(
          Call("A", "555-433-242", 5 * 60),
          Call("A", "555-433-242", 5 * 60)
        ),
        BigDecimal(25.2)
      ),
      FivePThanThreePTestCase(
        "2 call of 5 minutes and 1 call of 3 minutes",
        Seq(
          Call("A", "555-433-242", 5 * 60),
          Call("A", "555-433-242", 5 * 60),
          Call("A", "555-433-242", 3 * 60)
        ),
        BigDecimal(34.2)
      )
    ).foreach { fivePThanThreeP =>
      fivePThanThreeP.testName in {
        fivePThanThreeP.calls.foldLeft(BigDecimal(0))(
          (acc, call) => acc + FivePThenThreeP.calculate(call)
        ) should equal (fivePThanThreeP.cost)
      }
    }
  }
}
