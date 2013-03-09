package nu.ganslandt.commlog.tools

import org.joda.time.Duration

class EachRequestPresentation extends PairedPresentation {

  def handle(request: Request, response: LogLine) {
    val responseTime = new Duration(request.timestamp, response.timestamp)
    println(request.timestamp.toString("hh:mm:ss,SSS") + " " + request.id + " " + request.requestName + " " + responseTime.getMillis + " " + outstandingRequests.size)

    outstandingRequests = outstandingRequests - request.id
  }

  def eof() {}

}
