package nu.ganslandt.commlog.tools

import scala.io.Source

object Main {

  def main(args: Array[String]) {

    val config = new Config

    val parser = new scopt.mutable.OptionParser("bs-commlog-tools", "0.1") {
      help("h", "help", "Print this help message")
      opt("v", "verbose", "Verbose mode", { config.verbose = true })
      arglistOpt("[file]", "File to digest. If no file is specified, STDIN is used.", { v: String => config.file = Some(v) })
    }
    if (parser.parse(args)) {
      val source: Seq[Source] = config.file match {
        case Some(file) => List(Source.fromFile(file))
        case None => List(Source.fromInputStream(System.in))
      }
      var views: List[Presentation] = List(new SummaryPresentation, new PerQueryDetailsPresentation)
      if (config.verbose) views = new EachRequestPresentation :: views

      val digester = new LogParser(views)
      digester.digest(source)
    }
  }

  class Config {
    var verbose: Boolean = false
    var file: Option[String] = None
  }

}
