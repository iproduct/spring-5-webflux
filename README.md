# Reactive Microservices with Spring 5 WebFlux

**WebFlux functional reactive services demo presented by [IPT-Intellectual Products & Technologies @jProfessionals 2018](http://iproduct.org/en/presentation-winter-edition-2018-jprofessionals/)**

This is a demo application, which shows how to develop functional reactive (micro-)services using Spring 5 WebFlux, Spring Boot 2.0, Spring Data reactive repositories, MongoDB, reactive Spring Security and more.

You can find links to the presentation in [this post](https://www.slideshare.net/Trayan_Iliev/microservices-with-spring-5-webflux-jprofessionals).

All demos are using Gradle for building.

## How to run the demos

## reactive-streaming-demos (requires Java 9)

1. Be sure to configure `JAVA_HOME` environment variable to point to JDK 9, and `PATH` to include `bin` sub-folder of JDK 9 (for Windows). For Linux use something like `sudo  update-java-alternatives --list` and `sudo  update-java-alternatives --set [JDK/JRE name e.g. java-9-oracle]` to choose the java 9.
1. Build and run the Gradle application (`reactive-streaming-demos` module) - e.g. run `gradle bootRun` from the `reactive-streaming-demos` folder. The application main class is `org.iproduct.demos.spring.streamingdemos.ReactiveStreamingDemosApp`.
3. Open [http://localhost:9000/](http://localhost:9000/) for Java CPU Profiling demo, and [http://localhost:9000/quotes.html](http://localhost:9000/quotes.html) for Reactive Option Quotes demo respectively in your browser.

## Whats new in Spring 5

Spring 5 adds a plenty of new features. Following are my favourits:
- Reactive Programming Model
- Spring Web Flux
- Reactive DB repositories & integrations + hot event streaming: MongoDB, CouchDB, Redis, Cassandra, Kafka
- Testing improvements – WebTestClient (based on reactive WebFlux WebClient)
- JDK 8+ and Java EE 7+ baseline - see the `reactive-quotes` demo for Java 9 example
- Kotlin functional DSL
