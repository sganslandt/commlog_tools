package nu.ganslandt.commlog.tools

import org.scalatest.FlatSpec
import org.joda.time.DateTime

class EachRequestPresentationSpec extends FlatSpec {

  "An EachRequestPresentation" should "handle a Request" in pending

  it should "handle a Response" in pending

  it should "handle a Response without matching Request" in {
    new EachRequestPresentation().handle(new Response("id", DateTime.now, "payload"))
  }

}
