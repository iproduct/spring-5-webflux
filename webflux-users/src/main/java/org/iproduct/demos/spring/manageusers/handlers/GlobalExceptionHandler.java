package org.iproduct.demos.spring.manageusers.handlers;

import org.iproduct.demos.spring.manageusers.exceptions.CustomValidationException;
import org.iproduct.demos.spring.manageusers.exceptions.UserDataException;
import org.springframework.boot.web.reactive.error.ErrorWebExceptionHandler;
import org.springframework.http.codec.HttpMessageWriter;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.HandlerStrategies;
import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.result.view.ViewResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
public class GlobalExceptionHandler implements ErrorWebExceptionHandler {

    @Override
    public Mono<Void> handle(ServerWebExchange exchange, Throwable ex) {
        /* Handle exceptions here */
        if (ex instanceof UserDataException) {
            return ServerResponse.badRequest()
                    .syncBody("Invalid user data: " + ex.getMessage())
                    .flatMap(resp ->
                        resp.writeTo(exchange, new HandlerStrategiesResponseContext(HandlerStrategies.withDefaults())) );
        } else if (ex instanceof CustomValidationException) {
            return ServerResponse.badRequest().contentType(APPLICATION_JSON)
                    .syncBody(((CustomValidationException) ex).getErrors())
                    .flatMap(resp ->
                            resp.writeTo(exchange, new HandlerStrategiesResponseContext(HandlerStrategies.withDefaults())) );
        }

        return Mono.error(ex);  // not handled here
    }
}

class HandlerStrategiesResponseContext implements ServerResponse.Context {
    private HandlerStrategies strategies;

    public HandlerStrategiesResponseContext(HandlerStrategies strategies) {
        this.strategies = strategies;
    }

    @Override
    public List<HttpMessageWriter<?>> messageWriters() {
        return this.strategies.messageWriters();
    }

    @Override
    public List<ViewResolver> viewResolvers() {
        return this.strategies.viewResolvers();
    }
}
