package nu.ganslandt.commlog.tools

import com.github.nscala_time.time.Imports._

class SummaryPresentation extends PairedPresentation {

  private var responseTimes: List[Duration] = List()
  private var requestCount = 0
  private var responseCount = 0
  private var errorCount = 0

  private var requestsPerMinute: Map[DateTime, Int] = Map()

  override def handle(logLine: LogLine) {
    super.handle(logLine)

    logLine match {
      case l: Request => {
        requestCount += 1
        val minute = l.timestamp.withSecondOfMinute(0).withMillisOfSecond(0)
        val requestsForMinute = requestsPerMinute.get(minute) match {
          case None => 0
          case Some(c) => c
        }
        requestsPerMinute = requestsPerMinute + ((minute, requestsForMinute + 1))
      }
      case l: Response => responseCount += 1
      case l: Error => errorCount += 1
    }
  }

  def handle(request: Request, response: LogLine) {
    val responseTime = new Duration(request.timestamp, response.timestamp)

    responseTimes = responseTime :: responseTimes
  }

  def eof() {
    println(s"Summary: $requestCount requests, $responseCount responses, $errorCount errors")
    println(s"Error rate: ${errorCount / requestCount.asInstanceOf[Double]}%")
    println()

    val avgThroughput = requestsPerMinute.map(_._2).sum / requestsPerMinute.size
    val minThroughput = requestsPerMinute.minBy(_._2)._2
    val maxThroughput = requestsPerMinute.maxBy(_._2)._2
    println(s"Throughput (requests/minute)")
    println("avg\tmin\tmax\t")
    println(s"$avgThroughput\t$minThroughput\t$maxThroughput")
    println()

    val total = responseTimes.reduce((d1, d2) => d1 + d2).getMillis
    val min = responseTimes.min.getMillis
    val max = responseTimes.max.getMillis
    val avg = responseTimes.map(_.millis).sum / responseTimes.size
    val _95perc = responseTimes.sortBy(d => d.millis).apply(responseTimes.size * 95 / 100).millis
    val median = responseTimes.sortBy(d => d.millis).apply(responseTimes.size / 2).millis

    println("Response times")
    println(f"${"calls"}%10s ${"total"}%10s ${"min"}%8s ${"max"}%8s ${"avg"}%8s ${"95%"}%8s ${"median"}%8s")
    println(f"${responseTimes.size}%10d ${fmt(total)}%10s ${fmt(min)}%8s ${fmt(max)}%8s ${fmt(avg)}%8s ${fmt(_95perc)}%8s ${fmt(median)}%8s")
    println()
  }

}
