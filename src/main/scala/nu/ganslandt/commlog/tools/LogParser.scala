package nu.ganslandt.commlog.tools

import io.Source
import org.joda.time.DateTime
import LogParser._


class LogParser(views: Seq[Presentation]) {

  def digest(sources: Seq[Source]) {
    for (source <- sources)
      for (line <- source.getLines())
        handle(line)

    views.foreach(p => p.eof())
  }

  private def handle(logLine: String) {
    parse(logLine) match {
      case Some(parsed) => views.foreach(v => {
        v.handle(parsed)
      })
      case None => println("Failed to parse line: " + logLine)
    }
  }

}

object LogParser {

  def parse(logLine: String): Option[LogLine] = {
    val year = logLine.substring(0, 4)
    val month = logLine.substring(5, 7)
    val day = logLine.substring(8, 10)
    val hour = logLine.substring(11, 13)
    val minute = logLine.substring(14, 16)
    val s = logLine.substring(17, 19)
    val ms = logLine.substring(20, logLine.indexOf(' ', 20))
    val rest = logLine.substring(logLine.indexOf(" ", 20) + 1)
    val logLineType = rest.substring(7, rest.indexOf(':'))
    val id = rest.substring(rest.indexOf('[') + 1, rest.indexOf(']'))
    val requestType = rest.substring(rest.indexOf(']') + 1, rest.indexOf('('))
    val payload = rest.substring(rest.indexOf('(') + 1, rest.indexOf(')'))

    val timestamp = new DateTime(year.toInt, month.toInt, day.toInt, hour.toInt, minute.toInt, s.toInt, ms.toInt)
    logLineType match {
      case "Request" => Some(new Request(id, requestType, timestamp, payload))
      case "Response" => Some(new Response(id, timestamp, payload))
      case "Error" => Some(new Error(id, timestamp, payload))
      case _ => println(s"Wrong loglineType [${logLineType}]"); None
    }
  }

}