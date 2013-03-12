package nu.ganslandt.commlog.tools

trait PairedPresentation extends Presentation {

  var outstandingRequests: Map[String, Request] = Map()

  def handle(logLine: LogLine) {
    logLine match {
      case req: Request => outstandingRequests = outstandingRequests + ((req.id, req))
      case resp: Response => {
        outstandingRequests.get(resp.id).map(request => handle(request, resp))
        outstandingRequests = outstandingRequests - resp.id
      }
      case resp: Error => {
        outstandingRequests.get(resp.id).map(request => handle(request, resp))
        outstandingRequests = outstandingRequests - resp.id
      }
    }
  }

  def handle(request: Request, response: LogLine)

}
