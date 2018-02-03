# Reactive Microservices with Spring 5 WebFlux

**WebFlux functional reactive services demo presented by [IPT-Intellectual Products & Technologies @jProfessionals 2018](http://iproduct.org/en/presentation-winter-edition-2018-jprofessionals/)**

This is a demo application, which shows how to develop functional reactive (micro-)services using Spring 5 WebFlux, Spring Boot 2.0, Spring Data reactive repositories, MongoDB, reactive Spring Security and more.

You can find links to the presentation in [this post](https://www.slideshare.net/Trayan_Iliev/microservices-with-spring-5-webflux-jprofessionals).

All demos use Gradle for building.

## Whats new in Spring 5

Spring 5 adds a plenty of new features. Following are my favourits:
- Reactive Programming Model
- Spring Web Flux
- Reactive DB repositories & integrations + hot event streaming: MongoDB, CouchDB, Redis, Cassandra, Kafka
- Testing improvements – WebTestClient (based on reactive WebFlux WebClient)
- JDK 8+ and Java EE 7+ baseline - see the `reactive-quotes` demo for Java 9 example
- Kotlin functional DSL


## How to run the demos

### reactive-streaming-demos (requires Java 9)

1. Be sure to configure `JAVA_HOME` environment variable to point to JDK 9, and `PATH` to include `bin` sub-folder of JDK 9 (for Windows). For Linux use something like `sudo  update-java-alternatives --list` and `sudo  update-java-alternatives --set [JDK/JRE name e.g. java-9-oracle]` to choose the java 9.
2. Build and run the Spring Boot-Gradle application (`reactive-streaming-demos` module) - e.g. run `gradle bootRun` from the `reactive-streaming-demos` folder. The application main class is `org.iproduct.demos.spring.streamingdemos.ReactiveStreamingDemosApp`.
3. Open [http://localhost:9000/](http://localhost:9000/) for Java Processes CPU Profiling demo (using novelties in Java 9 Process API:  `ProcessHandle` and `ProcessInfo` classes), and [http://localhost:9000/quotes.html](http://localhost:9000/quotes.html) for Reactive Option Quotes demo respectively in your browser.


#### Streaming demo REST and SSE endpoints
Provides information about runiiing Java processes and streaming data about their CPU ussage. Second demo provides streaming data about **fictional** options quote prices. **All data is purely fictional - NO real stock prices are quoted :)**:

Method	| Path	| Description	| JSON | SSE Streaming
------------- | ------------------------- | ------------- |:-------------:|:-------------:|
GET	| /api/processes	| List all currently running Java processes	   |  	*  |  
SSE	| /api/cpu	| Get realtime stream of CPU consumption for all Java processes |   * |  *
SSE	| /api/quotes	| Get fictional options quote prices in realtime  |   * |  *


### webflux-users (requires Java 8)

1. Be sure to configure `JAVA_HOME` environment variable to point to **JDK 8**, and `PATH` to include `bin` sub-folder of **JDK 8** (for Windows). For Linux use something like `sudo  update-java-alternatives --list` and `sudo  update-java-alternatives --set [JDK/JRE name e.g. java-8-oracle]` to choose the **java 8**.
2. Install (if not already installed) latest version of MongoDB, create <local_database_folder>, and start MongoDB - e.g. run `mongod --dbpath="<local_database_folder>"`.
3. Build and run the Spring Boot-Gradle application (`webflux-users` module) - e.g. run `gradle bootRun` from the `webflux-users` folder. The application main class is `org.iproduct.demos.spring.manageusers.WebfluxUsersApplication`.
4. Open [http://localhost:8080/api/users](http://localhost:8080/api/users) in your browser - you should be asked to login using BASIC authentication with default admin cedentials - **user: admin, password: admin**. Should see something like: 
`[{"id":"","username":"admin","fname":"Default","lname":"Admin","role":"ADMIN","active":true}]`. You could use a REST client such as `Postman` or `curl` to read, create, update and delete users following the standard REST API conventions.
5. Run the unit tests (does not require server to be started) with: `gradle clean test` or `gradle clean test --info`


#### Users service
Provides CRUD operations for the **Users**:

Method	| Path	| Description	| Requires authentication | Admin only
------------- | ------------------------- | ------------- |:-------------:|:-------------:|
GET	| /api/users	| List all users	          |  	*  |  
GET	| /api/users/{userId}	| Get current account statistics	|    |  
POST	| /api/users	| Create new user	|  *  |  
PUT	| /api/users/{userId}	| Update user data	|  *  |  
DELETE | /api/users/{userId}	| Delete existing user	|  *  |  * 
