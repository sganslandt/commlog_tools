package nu.ganslandt.commlog.tools

import scala.io.Source

object Main {

  def main(args: Array[String]) {
    val source = if (args.length == 0) Source.fromInputStream(System.in) else Source.fromFile(args(0))
    val digester = new LogParser(List(new EachRequestPresentation()))
    digester.digest(source)
  }

}
