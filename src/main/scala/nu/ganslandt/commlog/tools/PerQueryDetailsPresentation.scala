package nu.ganslandt.commlog.tools

class PerQueryDetailsPresentation extends PairedPresentation {

  var perRequestDetails: Map[String, Stats] = Map()

  def handle(request: Request, response: LogLine) {
    val requestTypeDetails = perRequestDetails.get(request.name) match {
      case None => new Stats(request.name)
      case Some(stats) => stats
    }

    requestTypeDetails.handle(request, response)
    perRequestDetails = perRequestDetails + ((request.name, requestTypeDetails))
  }

  def eof() {
    var rank = 0
    val totalTime = perRequestDetails.values.map(_.totalTime).sum

    println("Per request summary")
    println(f"${"rank"}%5s ${"requestType"}%20s ${"calls"}%10s ${"response time"}%20s")
    for (stat <- perRequestDetails.values.toSeq.sortBy(-_.totalTime)) {
      rank += 1
      println(f"$rank%5d ${stat.requestType}%20s ${stat.requestCount}%10s ${fmt(stat.totalTime)}%10s (${100 * stat.totalTime / totalTime.asInstanceOf[Double]}%6.2f%)")
    }
    println()

    println("Per request details")
    for (stat <- perRequestDetails.values.toSeq.sortBy(-_.totalTime)) {
      println(s"${stat.requestType}: ${stat.requestCount} requests, ${fmt(stat.totalTime)} total response time")
      println(f"${"set"}%6s ${"calls"}%10s ${"response time"}%20s ${"min"}%8s ${"max"}%8s ${"avg"}%8s ${"median"}%8s")
      println(f"${"all"}%6s ${stat.requestCount}%10s ${fmt(stat.totalTime)}%10s (${100 * stat.totalTime / stat.totalTime.asInstanceOf[Double]}%6.2f%) ${fmt(stat.minTime)}%8s ${fmt(stat.maxTime)}%8s ${fmt(stat.avgTime)}%8s ${fmt(stat.medianTime)}%8s")
      val _95perc: Stats = stat.perc(95)
      println(f"${"95%"}%6s ${_95perc.requestCount}%10s ${fmt(_95perc.totalTime)}%10s (${100 * _95perc.totalTime / stat.totalTime.asInstanceOf[Double]}%6.2f%) ${fmt(_95perc.minTime)}%8s ${fmt(_95perc.maxTime)}%8s ${fmt(_95perc.avgTime)}%8s ${fmt(_95perc.medianTime)}%8s")
      val top5perc: Stats = stat.topPerc(5)
      println(f"${"top 5%"}%6s ${top5perc.requestCount}%10s ${fmt(top5perc.totalTime)}%10s (${100 * top5perc.totalTime / stat.totalTime.asInstanceOf[Double]}%6.2f%) ${fmt(top5perc.minTime)}%8s ${fmt(top5perc.maxTime)}%8s ${fmt(top5perc.avgTime)}%8s ${fmt(top5perc.medianTime)}%8s")
      println("Response time distribution")
      println(f"${"5ms"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(0, 5) / stat.requestCount)}")
      println(f"${"20ms"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(6, 20) / stat.requestCount)}")
      println(f"${"100ms"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(21, 100) / stat.requestCount)}")
      println(f"${"200ms"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(101, 200) / stat.requestCount)}")
      println(f"${"400ms"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(201, 400) / stat.requestCount)}")
      println(f"${"800ms"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(401, 800) / stat.requestCount)}")
      println(f"${"2s"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(801, 2000) / stat.requestCount)}")
      println(f"${"10s"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(2001, 10000) / stat.requestCount)}")
      println(f"${"30s"}%8s ${"#" * (72 * stat.requestCountBetweenMillis(100001, 300000) / stat.requestCount)}")
      println()
    }

  }

}
