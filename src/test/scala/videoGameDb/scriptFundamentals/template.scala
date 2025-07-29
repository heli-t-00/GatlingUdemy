package videoGameDb.scriptFundamentals

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class template extends Simulation {
  //Set up HTTP configuration used in all request
  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")



  //Create the scenario
  val scn = scenario("")
    .exec()


  //Run it
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)


}
