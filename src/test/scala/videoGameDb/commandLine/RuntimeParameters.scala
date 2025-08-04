package videoGameDb.commandLine

import io.gatling.core.Predef._
import io.gatling.core.structure.ChainBuilder
import io.gatling.http.Predef._


class RuntimeParameters extends Simulation {

  val httpProtocol = http.baseUrl("https://videogamedb.uk/api")
    .acceptHeader("application/json")

  // Runtime Parameters from Command Line (CLI) - these parameters from terminal command line using System.getProperty
  def USERCOUNT: Int = System.getProperty("USERS", "5").toInt
  def RAMPDURATION: Int= System.getProperty("RAMP_DURATION", "10").toInt
  def TESTDURATION: Int =System.getProperty("TEST_DURATION", "30").toInt
// System.getProperty() syntax - this method takes two string parameters: first parameter is the property name, second parameter is the default value
  // EXAMPLE: System.getProperty("propertyName", "defaultValue")


  //This block runs BEFORE the simulation starts. Prints out test configuration so can verify the values passed
  before {
    println(s"Running Test with ${USERCOUNT} users")
    println(s"Ramping users over ${RAMPDURATION} seconds")
    println (s"Total test duration: ${TESTDURATION} seconds")
  }

  //Defines a reusable chain of actions: Sends a GET request to /videogame, then WAITS 1 second before next action
  def getAllVideoGames(): ChainBuilder ={
    exec(
      http("Get all video games")
        .get("/videogame")
    ).pause(1)
  }
  // SCENARIO definition - Creates a scenarios called 'Run from command line', LOOPS forever (limited later by maxDuration, Repeatedly sends the getAllVideoGames request
  val scn = scenario("Run from command line")
    .forever{
      exec(getAllVideoGames())
    }

  //Load injection setup - launches the scenario with 5 sec pause, gradually ramp up uses (based on command line value or DEFAULT), a hard time limit for how lon the test should run is set(.maxDuration)
  setUp(
    scn.inject(
      nothingFor(5), // wait 5 seconds before starting
      rampUsers(USERCOUNT).during(RAMPDURATION) // gradually add users over RAMPDURATION
    )
  ).protocols(httpProtocol)
    .maxDuration(TESTDURATION) // stop the test after TESTDURATION

  /*Run this on command line in TERMINAL :
  mvn gatling:test -Dgatling.simulationClass=videoGameDb.commandLine.RuntimeParameters -DUSERS=10 -DRAMP_DURATION=20 -DTEST_DURATION=30

  ****ALWAYS CHECK****
  - Match your system property keys EXACTLY between Command Line and SCALA code
  Mismatch will cause the system to fall back to default

    */
}
