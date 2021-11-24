# Carlos Sedano

# Mancala Game

<!-- TABLE OF CONTENTS -->
<details open="open">
  <summary>Mancala Game</summary>
  <ol>
    <li>
      <a href="#synopsis">Synopsis</a>
      <ul>
        <li><a href="#built-with">Built With</a></li>
      </ul>
    </li>    
    <li>
      <a href="#gameplay">Gameplay</a>
    </li>
    <li>
      <a href="#getting-started">Getting Started</a>
      <ul>
        <li><a href="#prerequisites">Prerequisites</a></li>
        <li><a href="#installation">Installation</a></li>
      </ul>
    </li>
    <li><a href="#usage">Usage</a></li>
    <li><a href="#api-reference">Swagger UI</a></li>
    <li><a href="#tests">Tests</a></li>
    <li>
      <a href="#technical-information">Technical Information</a>
      <ul>
        <li><a href="#code-architecture">Code architecture</a></li>
      </ul>
    </li>
    <li><a href="#contact">Contact</a></li>
  </ol>
</details>

## Synopsis

The mancala games are a family of two-player turn-based strategy board games played with small stones, beans, or seeds
and rows of holes or pits in the earth, a board or other playing surface. The objective is usually to capture all or
some set of the opponent's pieces.- [Wikipedia](https://en.wikipedia.org/wiki/Mancala)

### Built With

* [IntelliJ IDEA](https://www.jetbrains.com/idea/)
* [Spring Boot](https://spring.io/projects/spring-boot)
* [Lombok](https://projectlombok.org/)
* [Swagger](https://swagger.io/specification/v2/)
* [Gradle](https://gradle.org/)

## Gameplay

* The board is designed for two players. There are six pits on each player's side and a Mancala (large pit) on either
  end.
* To start place 6 stones in each pit, leaving The Mancalas empty.
* Decide who goes first. (Randomly)
* One player picks up all the stones in one of their own pits.
* Starting with the next pit (counterclockwise), drop one stone in each pit including your own Mancala and skipping your
  opponent’s Mancala.
* If the last stone is dropped in your own Mancala, you get to play again.
* If the last stone is dropped in an empty pit on your own side and there are stones in the pit opposite the pit adhere
  you just dropped the last stone, then you get to capture your own stone and all the stones in the opposite pit and
  place this all in your own Mancala. Otherwise, it is the other players turn.
* Then all the pits on one side are empty, the other player gets to take all the stones on their own side and place
  those stones into their own Mancala.
* At the end, whoever has the most stones in their Mancala wins.



<!-- GETTING STARTED -->

## Getting Started

### Prerequisites

* Install [IntelliJ IDEA](https://www.jetbrains.com/idea/), configure & install plugins
    * Plugins - Press `Ctrl`+`Alt`+`S` / `Cmd`+`,` to open IDE settings and select **Plugins**. Find the plugin in
      the **Marketplace** and click **Install**. For more information
      see [IntelliJ oficial wiki](https://www.jetbrains.com/help/idea/managing-plugins.html#install_plugin_from_repo)
        * [google-java-format](https://plugins.jetbrains.com/plugin/8527-google-java-format)
    * Optional plugins
        * [Rainbow Brackets](https://plugins.jetbrains.com/plugin/10080-rainbow-brackets)
    * Enable annotation processing - Press `Ctrl`+`Alt`+`S` / `Cmd`+`,`  to open IDE settings and select **Build,
      Execution, Deployment** | **Compiler** | **Annotation Processors** and make
      sure ```Enable annotation processing``` is checked. For more information
      see [IntelliJ oficial wiki](https://www.jetbrains.com/help/idea/annotation-processors-support.html)

* Install [Gradle](https://gradle.org/install/)
* Install [Postman](https://www.postman.com/)

### Installation

1. Clean and build with Gradle
   ```sh
   ./gradlew clean build
   ```
3. Run project SpringBoot app on local with Gradle
   ```sh
   ./gradlew bootRun
   ```

## Usage

TODO: Write usage instructions

## Swagger UI

Swagger UI

http://localhost:8080/swagger-ui.html

![Swagger UI](documentation/imgs/swagger.png)

## Tests

1. Execute bundle of unit and integration tests
   ```sh
   ./gradlew clean test
   ```

## Technical Information

### Code architecture

**java/com/game/malanca**

- **config/apidocumentation** - Contains Swagger configuration class for API documentation
- **controller** - Contains all controllers (Our API endpoints). This controllers uses @Service classes located at
  business package
    - **error** - Contains response exception handler and all exception classes for Mancala game api
- **domain/dto** - Contains all objects related to Mancala game project
    - **requests** - Contains request objects for the game API (DTOs)
    - **responses** - Contains response objects for game API (DTOs)
- **mapper** - Contains mappers to build from a DTO request to a DTO response
- **service** - Contains all business logic
- **util** - Contains common resources for this project

**java/resources**

- **application.properties** - Contains all properties for Mancala game app

## Contact

[Carlos Sedano](https://www.linkedin.com/in/carlos-sedano)

TODO

run yarn windows problem fix
-> https://www.c-sharpcorner.com/article/how-to-fix-ps1-can-not-be-loaded-because-running-scripts-is-disabled-on-this-sys/

