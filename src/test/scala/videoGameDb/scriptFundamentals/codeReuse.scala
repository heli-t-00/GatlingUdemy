package videoGameDb.scriptFundamentals

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class codeReuse extends Simulation {
// HTTP protocol configuration
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  //Scenario Definition

  //CREATE methods/functions getAllVideoGames and getSpecificGame - this makes the code cleaner and easier to maintain
  def getAllVideoGames(): ChainBuilder = { //use ChainBuilder for chaining inside a scenario
    exec(http("Ger all video games")
      .get("/videogame") // API endpoint
      .check(status.is(expected = 200))) // check that the status code ==200
  }

  def getSpecificGame(): ChainBuilder = {
    exec(http("Get Specific Game")
      .get("/videogame/1") // Call game ID 1
      .check(status.in(expected = 200 to 210)))
  }
/*What is ChainBuilder - in Gatling, when you do something like exec(http(...))
it builds a chain of actions ie. make an HTTP request, wait, do another request
 ChainBuilder is Gatling's way of saying a "sequence of steps that can be reused inside a scenario.
 If a function returns a ChainBuilder, you can plug it into a scenario with .exec(...) */

  val scn = scenario("Code resue")
    .exec(getAllVideoGames()) // First call: Get all games
    .pause(5) // Wait 5 seconds
    .exec(getSpecificGame()) // Then call a specific game
    .pause(5) // Wait 5 seconds
    .exec(getAllVideoGames()) // Call all games

  //This reused the same methods multiple times instead of writing calls again with exec(http(...))



  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
