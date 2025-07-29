package videoGameDb.scriptFundamentals

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

//This example BUILDS on CodeReuse

class codeReuseLoop extends Simulation {
// HTTP protocol configuration
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")


  //Scenario Definition
// USE repeat blocks in script to repeat actions multiple of times
  // repeat(times = x) means "Do the following block of code X times in a row" this is helpful when testing API to simulate users making repeated requests. and test how the system performs under repeated usage

  // example 1: Send GET request to /videogame , check that the response status is 200(ok). ie. a user opens the app and refreshes the game list 3 times
  def getAllVideoGames(): ChainBuilder = {
    repeat(times =3){
      exec(http("Ger all video games")
        .get("/videogame") // API endpoint
        .check(status.is(expected = 200)))
    }

  }
// This example - repeats 5 times, each time, it replaces #{counter} with a number from 1-5, it also checks if each response status is between 200-210(success range) ie. the user is checking the details of 5 different video games, one by one.
  def getSpecificGame(): ChainBuilder = {
    repeat(times = 5, counterName = "counter") {
      exec(http("Get Specific Game")
        .get("/videogame/#{counter}")
        .check(status.in(expected = 200 to 210)))
    }

  }


  val scn = scenario("Code resue")
    .exec(getAllVideoGames()) // First call: Get all games 3 times
    .pause(5) // Wait 5 seconds
    .exec(getSpecificGame()) // Get specific games 1 to 5
    .repeat(times=2) { // run 'get all games' 3 times, then repeat all of that 2 times
      getAllVideoGames()
    }


  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

  // REPEAT helps simulate doing the same task multiple times. It is useful in load testing to check how systems behave under repetitive actions. Use counter (like 'counter' to change the request slightly on each repeat. 
}
