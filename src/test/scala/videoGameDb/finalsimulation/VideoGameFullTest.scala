package videoGameDb.finalsimulation

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class VideoGameFullTest extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")

  // ** BEFORE  BLOCKS - print out message at the start
//  before{
//    println(s"Running test with ${}")
//  }
  // **HTTP calls**
def authenticate(): ChainBuilder ={
  exec(http("Authenticate")
  .post("/authenticate"))
}

  def getAllVideoGames(): ChainBuilder = {
  exec(http("Get all video games")
    .get("/videogame")
    .check(status.is(expected = 200))
    )
  }
  // ** ADD OTHER HTTP CALLS ** /

  /* SCENARIO DESIGN */
val scn = scenario("Video Game Full Test")
  .exec(http("Get all video games")
  .get("/videogame"))
  // using the http calls, create a scenario that does the following:
  // 1. Get all games
  // 2. Create new Game (remember to authenticate first!)
  // 3. Get details of single game
  // 4. Delete game

  // ** SETUP LOAD SIMULATION
setUp(
scn.inject(atOnceUsers(1))
).protocols(httpProtocol)
  // create a simulation that has runtime parameters:
  // 1. Users
  // 2. Ramp up time
  // 3. Test duration

  // ** CUSTOM FEEDERS ** /

  // feeder to generate the JSON for creating a new game

  // ** VARIABLES FOR FEEDERS ** /

  // ** BEFORE AND AFTER BLOCKS
  // print out message at the start and end of the test


}
