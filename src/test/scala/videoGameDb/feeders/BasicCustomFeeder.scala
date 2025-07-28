package videoGameDb.feeders

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

//Performance test for a REST API endpoint that fetches video game data based on an ID.
// This script Run 1 virtual user who sends 10 requests to https://videogame.db.uk/api/videogame/{gameId}, where {gameId} ranges from 1 to 10. After each request, pause for 1 second. Verify that each response returns HTTP status 200(OK).
class BasicCustomFeeder extends Simulation {
  val httpProtocol = http.baseUrl("https://videogame.db.uk/api")
    .acceptHeader("application/json")

  val idNumbers = (1 to 10).iterator // creates an iterator over numbers 1 to 10

  val customFeeder = Iterator.continually(Map("gameId" -> idNumbers.next())) // creates a custom feeder that feeds a map with a key "gameId" and a value from idNumbers.next() every time it's called.
  //***NB*** Gatling will crash if there is more users when you RUN this with more than 1 user. Once .next() has been called 10 times by any user, it will throw NoSuchElementException and the feeder dies.

  //******Safe Custom feeder that generates random ID between 1 and 10 ===uncomment lines 19-20====
//  val customFeeder = Iterator.continually(Map("gameId" -> (util.Random.nextInt(10)+1)) // IDs: i to 10
//  )
// Can use this Safe Custom Feeder for MORE than one user

  // Method that repeats 10 times - feeds the gameID into the virtual users session
  // Send a GET request to the endpoint /videogame/#{gameId}, where #{gameId} is replaced by the value from the feeder.
  def getSpecificVideoGame() = {
    repeat(10) {
      feed(customFeeder)
        .exec(http("Get video game with id - #{gameId}")
          .get("/videogame/#{gameId}")
          .check(status.is(200))) // check response status is 200 OK
        .pause(1) // waits for 1 second between request
    }
  }

  val scn = scenario("Basic Custom Feeder")
    .exec(getSpecificVideoGame())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}
