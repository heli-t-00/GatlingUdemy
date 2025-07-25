package videoGameDb

import io.gatling.core.Predef._
import io.gatling.http.Predef._

import scala.concurrent.duration.DurationInt

class MyFirstTest extends Simulation{

  // 1 Http Configuration => where & how to send requests
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")
// Root API URL where all requests will be sent to the baseUrl. This is where every request will start with this.
//acceptHeader - adds an HTTP header do that the server knows you expect JSON responses.
  // when you call .get("/videogame") Gatling will hit https"//videogamedb.uk/api/videogame - setting up default HTTP settings

  // 2 Scenario Definition => what each user does
  val scn = scenario("My First Test") // creates a scenario called MyFirstTest
    .exec(http("Get all games") // executes one step in the scenario
      .get("/videogame")) // sends a GET request to /videogame (full URL is https://videogamedb.uk/api/videogame)
  // This scenario calls one API endpoint and stops


  // 3 Load Scenario - how many users & when i.e. this simulates the number of users. Change the injection profile to run MORE users ie. run 10 users change (1) to (10)
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

  // setUp(...) tells Gatling how to run the scenario
  //scn.inject(atOnceUsers(1)) - runs 1 user immediately. if the number is changed, it will run X number of users in parallel at the same time.
  // .protocols(httpProtocol) - uses the HTTP settings defined earlier.
  // This runs 1 users immediately - that user executes the "Get all games" request - test ends.

  // Ramp up users gradually for better load tests use (rampUsers(50).during(30.seconds) = this means 50 users spread over 30 seconds,  instead of atOnceUsers(1)

//  setUp(
//    scn.inject(rampUsers(50).during(30.seconds))
//  ).protocols(httpProtocol)
//
//// Constant users per second
//  setUp(
//    scn.inject(
//      constantUsersPerSec(5).during(60.seconds) //Runs 5 new users every 1 minute.
//    )
//  ).protocols(httpProtocol)
//

// COMBINE Multiple scenarios into ONE setUp
  // NB: Can not have MORE than ONE setUp(...)in the SAME file, otherwise Gatling will CRASH
  // To have multiple you need to write a setUp(...) that combines this EXAMPLE====

//  setUp(
//    scn1.inject(atOnceUsers(5)),
//    scn2.inject(rampUsers(20).during(30.seconds))
//  ).protocols(httpProtocol)
}