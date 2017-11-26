package org.iproduct.demos.spring.hellowebflux.handlers;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.demos.spring.hellowebflux.domain.Role;
import org.iproduct.demos.spring.hellowebflux.domain.User;
import org.iproduct.demos.spring.hellowebflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;

@Component
@Slf4j
public class UserHandler {

    @Autowired
    private UserRepository userRepo;

    public Mono<ServerResponse> findAllUsers(ServerRequest request) {
        return ServerResponse.ok().contentType(APPLICATION_JSON)
                .body(Flux.fromIterable(userRepo.findAll()), User.class);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.body(BodyExtractors.toMono(User.class))
            .map(user -> {
                log.debug("User: " +  user);
                if(user.getRole() == null) {
                    user.setRole(Role.CUSTOMER);
                }
                PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                user.setPassword(passwordEncoder.encode(user.getPassword()));
                user.setActive(true);
                return userRepo.save(user);
            })
            .flatMap(createdUser -> ServerResponse.created(
                    URI.create(request.path() + "/" + createdUser.getId())).syncBody(createdUser));
    }
}
