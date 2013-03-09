package nu.ganslandt.commlog.tools

import org.joda.time.Duration

class EachRequestPresentation extends Presentation {

  var outstandingRequests: Map[String, Request] = Map()

  def handle(logLine: LogLine) {
    logLine match {
      case req: Request => outstandingRequests = outstandingRequests + ((req.id, req))
      case resp: Response => outstandingRequests.get(resp.id).map(request => print(request, resp))
      case resp: Error => outstandingRequests.get(resp.id).map(request => print(request, resp))
    }
  }

  def print(request: Request, response: LogLine) {
    val responseTime = new Duration(request.timestamp, response.timestamp)
    println(request.timestamp.toString("hh:mm:ss,SSS") + " " + request.id + " " + request.requestName + " " + responseTime.getMillis + " " + outstandingRequests.size)

    outstandingRequests = outstandingRequests - request.id
  }

  def eof() {
  }

}
