package nu.ganslandt.commlog.tools

import com.github.nscala_time.time.Imports._
import com.github.nscala_time.time.TypeImports.DateTime

trait Presentation {

  def handle(logLine: LogLine)

  def eof()

  def fmt(millis: Long): String = {
    val seconds: Int = 1000
    val minutes: Int = 60 * 1000

    if (millis < 2 * seconds) millis + "ms"
    else if (millis < 2 * minutes) (millis / seconds) + "s"
    else if (millis < 60 * minutes) DateTimeFormat.forPattern("mm'm'ss's'").print(new DateTime(millis))
    else DateTimeFormat.forPattern("h'h'm'm's's'").print(new DateTime(millis))
  }

}
