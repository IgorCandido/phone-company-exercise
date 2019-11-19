import java.io.FileNotFoundException

import com.phone.io.LogReader.FileLogReader
import org.scalatest.{AsyncWordSpec, Matchers}

class FileLogReaderSpec extends AsyncWordSpec with Matchers {
  "FileReader" should {
    "Be able to read all lines of the file" in {
      FileLogReader
        .read(getClass.getResource("callsTemp1.log").getFile)
        .map(
          _ should equal(
            Seq("A 555-333-212 00:02:03", "A 555-433-242 00:06:41")
          )
        )
        .unsafeToFuture()
    }

    "Error when can't find file" in {
      FileLogReader
        .read("calls22.log")
        .attempt
        .map {
          _.fold(
            _ shouldBe a[FileNotFoundException],
            _ => fail("Should have failed to find file")
          )
        }
        .unsafeToFuture()
    }
  }
}
