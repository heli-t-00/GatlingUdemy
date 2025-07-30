package videoGameDb.feeders

import io.gatling.http.Predef._
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.util.HttpHelper.OkCodes.iterator

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import scala.util.Random

class ComplexCustomFeeder extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
    .contentTypeHeader("application/json")


  // Feeder Setup - using scala to randomly generate Int/Strings
  var idNumbers = (1 to 10).iterator // creates sequence from 1 to 10 - *** when using an iterator always use VAR instead of VAL because VAL cannot change***

  val rnd = new Random() // creates random object
  val now = LocalDate.now() // today's date
  val pattern = DateTimeFormatter.ofPattern("yyyy-MM-dd")// the date format

// Custom feeder - with random test data created using Feeder setup
  val customFeeder = Iterator.continually(Map(
    "gameId" -> idNumbers.next(),
//    "gameId" ->(rnd.nextInt(1000)+ 1), //Uncomment ln 27, Comment ln 26 - this use Random IDs to generate gameId
    "name" -> ("Game-" + randomString(length = 5)),
    "releaseDate" -> getRandomDate(now, rnd),
    "reviewScore" -> rnd.nextInt(100),
    "category" -> ("Category-" + randomString(length = 6)),
    "rating" -> ("Rating- " + randomString(length = 4))
  ))

// Utility methods - creates a random string of letters given length
  def randomString(length: Int) = {
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  } // scala code - randomly generate string

  //This method gets a random date up to 30 days in the past, formatted as yyyy-MM-dd
  def getRandomDate(startDate: LocalDate, random: Random): String = {
    startDate.minusDays(random.nextInt(30)).format(pattern)
  }

  // Authentication request - sends POST request to authenticate and saves the token as jwtToken
  def authenticate(): ChainBuilder = {
    exec(http(requestName = "Authenticate")
      .post("/authenticate")
      .body(StringBody(string = "{\n  \"password\": \"admin\",\n  \"username\": \"admin\"\n}"))
      .check(status.is(200))
      .check(jsonPath(path = "$.token").saveAs(key = "jwtToken")))
  }

  //Create New Game - using the custom feeder, creates 10 new games using data from the feeder
  //reads a JSON template from bodies/newGameTemplate.json

  def createNewGame(): ChainBuilder = {
    repeat(times = 10) {
      feed(customFeeder)
        .exec(http(requestName = "Create new game - #{name}")
          .post(url = "/videogame")
          .header("authorization", "Bearer #{jwtToken}")
          .body(ElFileBody(filePath = "bodies/newGameTemplate.json")).asJson
          .check(bodyString.saveAs(key = "responseBody")))
        .exec { session => println(session("responseBody").as[String]); session }
        .pause(1)
    }
  }
// Scenario and Setup
  val scn = scenario("Complex Custom Feeder")
    .exec(authenticate())
    .exec(createNewGame())

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}


