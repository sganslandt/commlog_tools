package nu.ganslandt.commlog.tools

abstract class PairedPresentation extends Presentation {

  var outstandingRequests: Map[String, Request] = Map()

  def handle(logLine: LogLine) {
    logLine match {
      case req: Request => outstandingRequests = outstandingRequests + ((req.id, req))
      case resp: Response => outstandingRequests.get(resp.id).map(request => handle(request, resp))
      case resp: Error => outstandingRequests.get(resp.id).map(request => handle(request, resp))
    }
  }

  def handle(request: Request, response: LogLine)

}
