package videoGameDb.feeders.simulations

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class basicLoadSimulation extends Simulation {
// HTTP Protocol Configuration - this defines the base set up for all HTTP requests in this simulation
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api") // Base URL - all request starts here
    .acceptHeader("application/json") // set Accept Header to expect JSON response
  // SCENARIO definition
  // Scenario represents user journey
  val scn = scenario("Basic Load Simulation") // creates a scenarion with descriptive name
    .exec(getAllVideoGames()) // Step1: Execute the getAllVideoGames function
    .pause(5) // Step2: Wait 5 seconds (simulates user thinking time)
    .exec(getSpecificGame()) // Step3: Execute the getSpecificGame function
    .pause(5) // Step4: wait another 5 seconds
    .exec(getAllVideoGames()) // Step5: Execute getAllVideoGames again

  //HELPER function 1: Get All Video Games - Chainbuilder allows to return a reusable chain of actions
  def getAllVideoGames(): ChainBuilder = {
    exec(
      http("Get all video games") //Request name (appears in report)
        .get("/videogame") // HTTP GET request to /videogame endpoint
    )
  }
//HELPER function 2: Get Specific Game - reusable function for getting specific game by ID
  def getSpecificGame(): ChainBuilder = {
    exec(
      http("Get Specific Game")
        .get("/videogame/2") // HTTP GET request to/videogame/2 (get game ID 2)
    )
  }


  //LOAD INJECTION SETUP - define how many users & how they should be injected into the system
  setUp(
    scn.inject( // Apply injection patter to scenario
      nothingFor(5), // wait 5s before starting any users
      atOnceUsers(5), // Inject 5 users immediately (after 5s wait)
      rampUsers(10).during(10) // Gradually inject 10 more users over 10 seconds
    ).protocols(httpProtocol) // Apply the HTTP protocol configuration to this scenario
  )

}
