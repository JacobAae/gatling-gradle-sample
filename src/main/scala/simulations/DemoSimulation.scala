package sdu

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class DemoSimulation extends Simulation {

  def URL = System.getProperty("URL")
  def users = Integer.getInteger("users", 5).doubleValue()
  def period = Integer.getInteger("seconds", 30)

  val httpConf = http
    .baseURL(URL)
//    .acceptHeader("text/json,application/json;q=0.9,*/*;q=0.8")
    .doNotTrackHeader("1")
    .acceptLanguageHeader("en-US,en;q=0.5")
    .acceptEncodingHeader("gzip, deflate")
    .userAgentHeader("Mozilla/5.0 (Windows NT 5.1; rv:31.0) Gecko/20100101 Firefox/31.0")

  object MyTest {
    val simpletest = exec(http("loadtest")
      .get("/")
      .check( status.is(200) )    // Check at kaldet gik godt
      .check( regex("Ratpack").exists )    // Check at Ratpack indgår
    )
  }

  val scn = scenario("loadtest")
    .exec(MyTest.simpletest)

  setUp(
    scn.inject(
//      constantUsersPerSec(users) during (period seconds) randomized
      rampUsersPerSec(20) to users during(period seconds)
    )
  ).protocols(httpConf)

}
