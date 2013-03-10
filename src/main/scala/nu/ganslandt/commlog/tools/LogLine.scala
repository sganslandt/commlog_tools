package nu.ganslandt.commlog.tools

import com.github.nscala_time.time.Imports._

abstract class LogLine(val id: String, val timestamp: DateTime, val payload: String)

class Request(id: String, val name: String, timestamp: DateTime, payload: String) extends LogLine(id, timestamp, payload)

class Response(id: String, timestamp: DateTime, payload: String) extends LogLine(id, timestamp, payload)

class Error(id: String, timestamp: DateTime, payload: String) extends LogLine(id, timestamp, payload)

