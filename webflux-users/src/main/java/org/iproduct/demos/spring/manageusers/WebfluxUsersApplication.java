package org.iproduct.demos.spring.manageusers;

import org.iproduct.demos.spring.manageusers.handlers.UserHandler;
import org.springframework.boot.ExitCodeGenerator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;

@SpringBootApplication
@EnableWebFlux
public class WebfluxUsersApplication {

    private static ConfigurableApplicationContext appContext;

    @Bean
    public RouterFunction<ServerResponse> routes(UserHandler userHandler) {
        return RouterFunctions.nest(RequestPredicates.path("/api/users"),
                RouterFunctions
                        .route(GET(""), userHandler::findAllUsers)
                        .andRoute(GET("/{id}"), userHandler::findUser)
                        .andRoute(POST("").and(contentType(APPLICATION_JSON)), userHandler::createUser)
                        .andRoute(PUT("/{id}").and(contentType(APPLICATION_JSON)), userHandler::editUser)
                        .andRoute(DELETE("/{id}"), userHandler::deleteUser)
        );
    }


    @Bean
    public ExitCodeGenerator exitCodeGenerator() {
        return new ExitCodeGenerator() {
            @Override
            public int getExitCode() {
                return 0;
            }
        };
    }

    public static void main(String[] args) throws InterruptedException {
        appContext = SpringApplication.run(WebfluxUsersApplication.class, args);
    }

}

