package videoGameDb.feeders
import io.gatling.http.Predef._
import io.gatling.core.Predef._
import org.checkerframework.checker.units.qual.Length

import scala.util.Random

class ComplexCustomFeeder extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  val idNumbers = (1 to 10).iterator
  val rnd = new Random()
  def randomString(length: Int): Unit ={
    rnd.alphanumeric.filter(_.isLetter).take(length).mkString
  }
  val scn = scenario( "Commplex Custom Feeder")
  val customFeeder=Iterator.continually(Map(
    "gameId" -> idNumbers.next()
    "name" -> ("Game-"+ randomString(length = 5)),
    "releaseDate" -> ???,
    "reviewScore" -> ???,
    "category" -> ???,
    "rating" -> ???
  ))

  setUp(
    scn.inject(atOnceUsers(1))
  ).protocols(httpProtocol)
}


