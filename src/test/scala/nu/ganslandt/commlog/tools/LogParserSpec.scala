package nu.ganslandt.commlog.tools

import org.scalatest._
import matchers.ShouldMatchers


class LogParserSpec extends FlatSpec with ShouldMatchers {

  "A LogParser" should "parse a proper Request" in {
    val parsed = LogParser.parse(
      "2013-02-09 22:03:28,880 INFO   Request: [604ef48c-6f59-4665-a507-6e178e4f9801] withdrawAndDeposit({" +
        "callerId='incomingId', callerPassword='incomingPassword', playerName='Boerboel86', withdraw=0.4, deposit=0.0, " +
        "currency='EUR', transactionRef='102154993', gameRoundRef='42520956', gameId='eldorado_sw', " +
        "reason='GAME_PLAY_FINAL', sessionId='1360436427666-12817-V60YIRY042LV3', any=null})"
    )

    assert(parsed.isInstanceOf[Some[Request]])
  }

  it should "not parse invalid input" in pending

  it should "parse a proper Response" in pending


}
