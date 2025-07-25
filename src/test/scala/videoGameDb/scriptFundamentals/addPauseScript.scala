package videoGameDb.scriptFundamentals

import io.gatling.core.scenario.Simulation
import io.gatling.http.Predef._
import io.gatling.core.Predef._

import scala.concurrent.duration.DurationInt

class addPauseScript extends Simulation {
  //1. Define the HTTP configuration (base URL and headers)
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api") // Base URL for all requests
    .acceptHeader("application/json") // Expect JSON response from server

  //2. Define Scenario (the sequence of actions a virtual user will perform)

  val scn = scenario("Video Game Db - 3 calls") // Scenario name for reporting
    //----First Request----
    .exec(http("Get all video games - 1st call") // name for this HTTP request
      .get("/videogame"))
    // GET request to fetch all video games
    .pause(5) // WAIT for 5 seconds before next action
    //----Second Request----
    .exec(http("Get specific game") // name for this request
      .get("/videogame/1"))
    // GET request for game with ID 1
    .pause(1, 10) // WAIT randomly between 1 - 10 seconds
    //----Third Request----
    .exec(http("Get all video games - 2nd call") // name for last request
      .get("/videogame"))
    // Fetch all games again
    .pause(3000.milliseconds) // WAIT exactly 3 seconds

  //3. Define the load setup (how many users, how they're injected)
  setUp(
    scn.inject(atOnceUsers(1)) // simulate 1 user running this scenario once
  ).protocols(httpProtocol) // Apply the HTTP config defined above


}
