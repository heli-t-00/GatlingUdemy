# 📘 Technical Notes – Gatling Performance Testing Project

## 🧠 Overview
Following the Udemy course, instructor: James Willett.
- Code along from Section 4: Dependency Installation & Gatling Project Configuration


## 🛠️ Tech Stack
| Component      | Details                     |
|----------------|-----------------------------|
| Load Testing   | [Gatling](https://gatling.io/) |
| Language       | Scala                       |
| Build Tool     | sbt                         |
| IDE            | IntelliJ IDEA               |
|  |

---

# Writing a FULL SIMULATION for videogamedb.uk

Guide to setting up a full simulation using Gatling for testing the [videogamedb.uk](https://www.videogamedb.uk/swagger-ui/index.html#/) API.

## 1. Create a Scala Class File
## 2. Import the Required Gatling Libraries

```scala
import io.gatling.core.Predef._
import io.gatling.http.Predef._
```
## 3. Extend the Simulation Class
```scala
class VideoGameFullTest extends Simulation {
  // Simulation code goes here
}
```
## 4. Scenario Design - Define HTTP calls
Define HTTP Calls for:
- Get all games: GET /videogame
- Create new game: POST /videogame
- Authenticate: POST /authenticate
- Get details of a single game (by ID): GET /videogame/{id}
- Delete game: DELETE /videogame/{id}

## 5. Create Scenario
```scala
val scn = scenario ("Full Video Game DB Test")
  .exec(http("Get ALL Games")
  .get("/videogame"))
  .pause(1)
// Add additional HTTP Calls (eg. authenticate, post, get by ID, delete)
```

## 6. Setup Runtime Parameters
```scala
setUp(
  scn.inject(atOnceUsers(1)) // or rampUsers(), constantUserPerSec(), etc
).protocols(httpProtocol)
``` 

## 7. Add a Custom Feeder
Use a feeder to provide dynamic data for test runs
```scala 
val customFeeder = csv("data/gameData.csv").circular

val scn = scenario("Game Data Test")
.feed(customFeeder)
.exec(http("Create Game")
.post("/videogame")
.body(StringBody("""{ "id": "${id}", "name": "${name}", "releaseDate": "${releaseDate}" }""")).asJson)
```

## Add BEFORE and AFTER Hooks

```scala
before {
  println("Starting the Video Game DB simulation...")
}

after {
  println("Finished executing the simulation.")
}
```

# NOTES
- Ensure the feeder file (gameData.csv) is placed in the resources directory
- Configure httpProtocol with your base URL:
```scala
val httpProtocol = http
  .baseUrl("https://videogamedb.uk") // Replace with actual base URL
  .acceptHeader("application/json")
```