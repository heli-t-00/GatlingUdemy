package videoGameDb.scriptFundamentals

import io.gatling.core.Predef._
import io.gatling.http.Predef._

class checkResponseBodyAndExtract extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  val scn = scenario("check with JSON path")
    .exec(http("Get specific game")
      .get("/videogame/1")
      .check(jsonPath("$.name").is(expected = "Resident Evil 4"))) //**** this is a response check using JSONPath expression. $.name means=> "look for the field name at the root of the JSON object" .is("Resident Evil 4") means=> "assert that the extracted value equals Resident Evil 4"

    /*NB: in this script, Gatling will Parse the HTTP response as JSON, find the field 'name', COMPARE its value with 'Resident Evil 4' and MARK request as OK if it matches or KO if it does not match/exist */

    //EXTRACT a value from JSON array
    .exec(http("Get all video games")
      .get("/videogame")
      .check(jsonPath("$[1].id").saveAs("gameId")))
    //expects a JSON array $[1] = the second item in the array .id= extract id value, store the extracted value into Gatling session variable gameId
    .exec { session => println(session); session }

    //Print extracted gameId to Gatling logs
    .exec { session =>
      println("***Extracted gameId***: " + session("gameId").as[String])
      session
    }

    // Use EXTRACTED value Eg.  use extracted "gameId" in next API call
    .exec(http("Get specific game")
      .get("/videogame/#{gameId}") // uses the saved #{gameId} =>becomes GET /videogame/2
      .check(jsonPath("$.name").is("Gran Turismo 3"))// checks if the JSON name is "..."
      .check(bodyStream.saveAs("responseBody")))


    .exec { session => println(session("responseBody").as[String]); session }


  // Runs the scenario once per 1 user
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

  /* ---KEY LEARNING---
  1.JSONPath for checking values
   $.field -> root field
   $[1].field -> second array elements field

  2. Response checks
  .is("value") -> must equal this value
  if mismatch -> request marked KO in Gatling report

  3. Extracting & chaining
  .saveAs("variableName") -> stores response value for NEXT request
  #{variableName} -> use it in later request

  4. Failure cases
   if no 2nd game -> $[1].id fails -> gameId not saved ->next request fails
   if game name changes ->check fails
  */

}
