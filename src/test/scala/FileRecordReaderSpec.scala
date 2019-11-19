package scala

import com.phone.errors.PhoneError
import com.phone.io.RecordReader.FileRecordReader
import com.phone.models.Call
import org.scalatest.{Matchers, WordSpec}

class FileRecordReaderSpec extends WordSpec with Matchers {
  "FileRecorder" should {
    case class FileRecordTestCase(testName: String,
                                  lines: Seq[String],
                                  results: Seq[Call])

    List(
      FileRecordTestCase(
        "Be able to read on valid call",
        Seq("A 555-333-212 00:02:03"),
        Seq(Call("A", "555-333-212", 2 * 60 + 3))
      ),
      FileRecordTestCase(
        "Be able to read several valid call",
        Seq("A 555-333-212 00:02:03", "A 555-433-242 00:06:41"),
        Seq(
          Call("A", "555-333-212", 2 * 60 + 3),
          Call("A", "555-433-242", 6 * 60 + 41)
        )
      )
    ).foreach { fileRecordTestCase =>
      fileRecordTestCase.testName in {
        FileRecordReader
          .readRecords(fileRecordTestCase.lines)
          .fold(
            errors => fail(errors.head),
            _ should equal(fileRecordTestCase.results)
          )
      }
    }
  }

  "FileReader invalid cases" should {
    case class FileRecordTestCase(testName: String,
                                  lines: Seq[String],
                                  error: PhoneError)
    List(
      FileRecordTestCase(
        "Report error when cannot parse Call",
        Seq("A 00:02:03"),
        PhoneError.FailedToParseCall
      ),
      FileRecordTestCase(
        "Report error when last line cannot be parsed",
        Seq("A 555-333-212 00:02:03", "A 00:02:03"),
        PhoneError.FailedToParseCall
      ),
      FileRecordTestCase(
        "Report error when first line cannot be parsed",
        Seq("A 00:02:03", "A 555-333-212 00:02:03"),
        PhoneError.FailedToParseCall
      )
    ).foreach { fileRecordTestCase =>
      fileRecordTestCase.testName in {
        FileRecordReader
          .readRecords(fileRecordTestCase.lines)
          .fold(
            errors =>
              errors.toChain.toList should contain(fileRecordTestCase.error),
            _ => fail("Should not be able to parse one of the Calls")
          )
      }
    }
  }

  "FileRecordReader when time parseError" should {
    "Properly return parse error when failing to read time" in {
      FileRecordReader
        .readRecords(Seq("A 555-333-212 00:02x:03"))
        .fold(
          errors =>
            errors.toChain.toList(0) shouldBe a[PhoneError.FailedToParseTime],
          _ => fail("Should not be able to parse one of the Calls")
        )
    }
  }
}
