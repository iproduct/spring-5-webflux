package org.iproduct.demos.spring.streamingdemos;

import org.iproduct.demos.spring.streamingdemos.handlers.ReactiveProfilerHandler;
import org.iproduct.demos.spring.streamingdemos.handlers.ReactiveQuotesHandler;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
public class ReactiveStreamingDemosApp {

    @Bean
    public RouterFunction<ServerResponse> routes(ReactiveProfilerHandler profilerHandler, ReactiveQuotesHandler qoutesHandler) {

        return route(GET("/api/processes").and(accept(APPLICATION_JSON)), profilerHandler::getProcesses)
                .andRoute(GET("/api/cpu").and(accept(APPLICATION_STREAM_JSON)), profilerHandler::streamCpu)
                .andRoute(GET("/api/cpu").and(accept(MediaType.TEXT_EVENT_STREAM)), profilerHandler::streamCpuSSE)
                .andRoute(GET("/api/quotes").and(accept(APPLICATION_STREAM_JSON)), qoutesHandler::streamQuotes)
                .andRoute(GET("/api/quotes").and(accept(MediaType.TEXT_EVENT_STREAM)), qoutesHandler::streamQuotesSSE);

    }

    public static void main(String[] args) {
        SpringApplication.run(ReactiveStreamingDemosApp.class, args);
    }

}

