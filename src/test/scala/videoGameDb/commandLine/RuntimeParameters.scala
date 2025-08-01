package videoGameDb.commandLine

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class RuntimeParameters extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")


  def getAllVideoGames(): ChainBuilder ={
    exec(
      http("Get all video games")
        .get("/videogame")
    ).pause(1)
  }
  val scn = scenario("Run from command line")
    .forever{
      exec(getAllVideoGames())
    }

  setUp(
    scn.inject(
      nothingFor(5),
      rampUsers(10).during(20)
    )
  ).protocols(httpProtocol)
    .maxDuration(20)


//Run this in terminal - "
}
