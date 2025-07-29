package videoGameDb.feeders
import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._

class CSVFeeder extends Simulation{

  //Set up HTTP configuration used in all request

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

val csvFeeder = csv("data/gameCsvFile.csv").circular // test data is hardcoded into script
  //Methods used with feeders - examples
  //.circular - picks rows in order, one by one from csv, when it reaches last row, it goes back to the first row and repeats. This is useful when you want each user to get predictable, repeatable data cycling through the dataset.
  //.random - picks rows randomly from csv every time it needs new data, useful when you want the test data to appear more varied and less predictable

  def getSpecificVideoGame(): ChainBuilder = {
    repeat(10) {
      feed(csvFeeder)
        .exec(http("Get video game with name - #{name}")
          .get("/videogame/#{gameId}")
          .check(jsonPath("$.name").is("#{name}"))
          .check(status.is(200)))
        .pause(1)
    }
  }



  //Create the scenario
  val scn = scenario("Csv feeder test")
    .exec(getSpecificVideoGame())


  //Run it
  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)

}
