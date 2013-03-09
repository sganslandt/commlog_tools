package nu.ganslandt.commlog.tools

import io.Source
import org.joda.time.DateTime
import LogParser._


class LogParser(views: Seq[Presentation]) {

  def digest(source: Source) {
    for (line <- source.getLines())
      handle(line)

    views.foreach(p => p.eof())
  }

  private def handle(logLine: String) {
    parse(logLine) match {
      case Some(parsed) => views.foreach(v => { v.handle(parsed) })
      case None => println("Failed to parse line: " + logLine)
    }
  }

}

object LogParser {

  def parse(logLine: String): Option[LogLine] = {
    lineP1 findFirstIn logLine match {
      case Some(lineP1(year, month, day, hour, minute, s, ms, logLineType, id, requestType, payload)) => {
        val timestamp = new DateTime(year.toInt, month.toInt, day.toInt, hour.toInt, minute.toInt, s.toInt, ms.toInt)
        logLineType match {
          case "Request" => Some(new Request(id, requestType, timestamp, payload))
          case "Response" => Some(new Response(id, timestamp, payload))
          case "Error" => Some(new Error(id, timestamp, payload))
        }
      }
      case None => None
    }
  }

  private val lineP1 = """(\d{4})-(\d{2})-(\d{2}) (\d{2}):(\d{2}):(\d{2}),(\d{1,3}) .* (\w+): \[(.*)\] (.*)\((.*)\)""".r

}