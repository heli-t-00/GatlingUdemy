package videoGameDb.scriptFundamentals

import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt

class checkResponseCode extends Simulation {
  //1. Define the HTTP configuration (base URL and headers)
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api") // Base URL for all requests
    .acceptHeader("application/json") // Expect JSON response from server

  //2. Define Scenario (the sequence of actions a virtual user will perform)

  val scn = scenario("Video Game Db - 3 calls")
    //----First Request----
    .exec(http("Get all video games - 1st call")
      .get("/videogame")
      .check(status.is(expected = 200)))

    .pause(5)
    //----Second Request----
    .exec(http("Get specific game")
      .get("/videogame/1")
    .check(status.in(expected = 200 to 210))) //verifies the HTTP response status code is within range ie. if response returns 200, 201...up to 210 the check will PASS. if the check returns outside range - check will FAIL.

    .pause(1, 10)
    // WAIT randomly between 1 - 10 seconds
    //----Third Request----
    .exec(http("Get all video games - 2nd call")
      .get("/videogame").check(status.not(expected = 404), status.not(expected = 500)))

    .pause(3000.milliseconds)

  //3. Define the load setup (how many users, how they're injected)
  setUp(
    scn.inject(atOnceUsers(1)) // simulate 1 user running this scenario once
  ).protocols(httpProtocol) // Apply the HTTP config defined above


}
