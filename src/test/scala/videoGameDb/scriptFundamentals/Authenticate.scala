package videoGameDb.scriptFundamentals
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class Authenticate extends Simulation{

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")


  //Step 1: Authenticate and save JWT token
  def authenticate(): ChainBuilder = {
    exec(http("Authenticate") // name HTTP request
      .post("/authenticate") // POST request to / authenticate endpoint
      .body(StringBody("{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}")) // Send login credentials in JSON format
      .check(status.is(200)) // verify the response status is 200 OK
      .check(jsonPath("$.token").saveAs("jwtToken"))) //save token to use in later request
  }
  //Step 2: Use token to create a new video game entry
  def createNewGame():ChainBuilder = {
    exec(http("Create new game")
      .post("/videogame")
      .header("Authorization", "Bearer #{jwtToken}")
      .body(StringBody(
        "{\n  \"category\": \"Platform\",\n  \"name\": \"Mario\",\n  \"rating\": \"Mature\",\n  \"releaseDate\": \"2012-05-04\",\n  \"reviewScore\": 85\n}"
      )))
  }

  //Create the scenario
  val scn = scenario("Authenticate") // Name of the scenario
    .exec(authenticate()) // Step 1 Authenticate and get token
    .exec(createNewGame()) // Step 2 Create game using token

  //Run it
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

  /*SUMMARY of flow
  * Authenticate-> send username/password  -> save JWT token from response
  * Create New Game -> use saved token in Authorisation header -> POST new game data
  * Scenario -> Chain the steps together
  * Run -> Execute scenario with 1 user */
}
