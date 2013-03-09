package nu.ganslandt.commlog.tools

trait Presentation {

  def handle(logLine: LogLine)

  def eof()

}
