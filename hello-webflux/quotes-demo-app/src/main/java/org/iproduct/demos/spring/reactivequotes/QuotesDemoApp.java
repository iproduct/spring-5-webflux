package org.iproduct.demos.spring.reactivequotes;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.reactive.config.EnableWebFlux;

@SpringBootApplication
@EnableWebFlux
public class QuotesDemoApp {

//    @Bean
//    public RouterFunction<ServerResponse> routes(ReactiveQuotesHandler qoutesHandler) {
//
//        return route(GET("/api/quotes").and(accept(APPLICATION_STREAM_JSON)), qoutesHandler::streamQuotes)
//                .andRoute(GET("/api/quotes").and(accept(MediaType.TEXT_EVENT_STREAM)), qoutesHandler::streamQuotesSSE);
//
//    }

    public static void main(String[] args) {
        SpringApplication.run(QuotesDemoApp.class, args);
    }

}

