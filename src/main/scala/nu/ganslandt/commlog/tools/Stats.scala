package nu.ganslandt.commlog.tools

import com.github.nscala_time.time.Imports._

class Stats(val requestType: String) {
  def this() { this("N/A") }
  def this(requestType: String, responseTimes0: List[Duration]) {
    this(requestType)
    responseTimes = responseTimes0
  }

  def handle(request: Request, response: LogLine) {
    val responseTime = new Duration(request.timestamp, response.timestamp)

    responseTimes = responseTime :: responseTimes
  }

  def perc(perc: Int): Stats = {
    new Stats(requestType, responseTimes.sortBy(d => d.millis).take(responseTimes.size * perc / 100))
  }

  def topPerc(perc: Int): Stats = {
    new Stats(requestType, responseTimes.sortBy(d => d.millis).drop(responseTimes.size * (100 - perc) / 100))
  }

  def requestCount: Int = responseTimes.size
  def totalTime: Long = responseTimes.reduce((d1, d2) => d1 + d2).getMillis
  def minTime: Long = responseTimes.min.getMillis
  def maxTime: Long = responseTimes.max.getMillis
  def avgTime: Long = responseTimes.map(_.millis).sum / responseTimes.size
  def medianTime: Long = responseTimes.sortBy(d => d.millis).apply(responseTimes.size / 2).millis

  def requestCountBetweenMillis(aboveMillis: Int, belowMillis: Int): Int = responseTimes.filter(d => d.millis > aboveMillis && d.millis < belowMillis).size

  private var responseTimes: List[Duration] = List()

}