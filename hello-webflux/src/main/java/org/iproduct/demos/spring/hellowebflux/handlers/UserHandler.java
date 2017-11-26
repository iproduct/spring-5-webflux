package org.iproduct.demos.spring.hellowebflux.handlers;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.demos.spring.hellowebflux.domain.Role;
import org.iproduct.demos.spring.hellowebflux.domain.User;
import org.iproduct.demos.spring.hellowebflux.exceptions.UsernDataException;
import org.iproduct.demos.spring.hellowebflux.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyExtractors;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.net.URI;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.badRequest;
import static org.springframework.web.reactive.function.server.ServerResponse.created;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;

@Component
@Slf4j
public class UserHandler {

    @Autowired
    private UserRepository userRepo;

    public Mono<ServerResponse> findAllUsers(ServerRequest request) {
        return ok().contentType(APPLICATION_JSON)
                .body(userRepo.findAll(), User.class);  // .body(Flux.fromIterable(userRepo.findAll()), User.class);
    }

    public Mono<ServerResponse> findUser(ServerRequest request) {
        String userId = request.pathVariable("id");
        if (!isValidId(userId)) return badRequest().syncBody("Error: Invalid URL user id: " + userId);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return userRepo.findById(userId)
                .flatMap(user -> ok()
                        .contentType(APPLICATION_JSON)
                        .body(fromObject(user)))
                .switchIfEmpty(notFound);
    }

    public Mono<ServerResponse> createUser(ServerRequest request) {
        return request.body(BodyExtractors.toMono(User.class))
                .filterWhen(user -> userRepo.findOneByUsername(user.getUsername())
                        .hasElement()
                        .map(success -> {
                            log.debug("Username exists: " + user.getUsername());
                            return !success;
                        }))
                .flatMap(user -> {                                    // .map(user -> {
                    if (user.getRole() == null) {
                        user.setRole(Role.CUSTOMER);
                    }
                    PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();
                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                    user.setActive(true);
                    log.debug("About to save User: " + user);
                    return userRepo.save(user);
                })
                .log()
                .flatMap(createdUser -> created(
                        URI.create(request.path() + "/" + createdUser.getId()))
                        .contentType(APPLICATION_JSON)
                        .syncBody(createdUser))
                .switchIfEmpty(
                        badRequest()
                                .syncBody("Error: Username already registered."));
    }

    public Mono<ServerResponse> editUser(ServerRequest request) {
        String userId = request.pathVariable("id");
        if (!isValidId(userId)) return badRequest().syncBody("Error: Invalid URL user id: " + userId);

        return request.body(BodyExtractors.toMono(User.class))
                .zipWith(userRepo.findById(Mono.justOrEmpty(userId)))
                .flatMap((Tuple2<User, User> tuple) -> {
                    User reqUser = tuple.getT1();
                    User foundUser = tuple.getT2();
                    if (!foundUser.getId().equals(reqUser.getId())
                            || !foundUser.getUsername().equals(reqUser.getUsername()))
                        throw new UsernDataException("Username and Id can not be changed: " + reqUser.getUsername() + " - " + reqUser.getId());
                    User resultUser = User.builder() // merging user data
                            .id(foundUser.getId())
                            .username(foundUser.getUsername()) // username canot be changed
                            .password(foundUser.getPassword()) // password change should be requested separately
                            .fname(reqUser.getFname() != null && reqUser.getFname().length() > 0 ?
                                    reqUser.getFname() : foundUser.getFname())
                            .lname(reqUser.getLname() != null && reqUser.getLname().length() > 0 ?
                                    reqUser.getLname() : foundUser.getLname())
                            .role(reqUser.getRole() != null ?
                                    reqUser.getRole() : foundUser.getRole())
                            .active(reqUser.isActive())
                            .build();
                    log.debug("About to edit User: " + resultUser);
                    return userRepo.save(resultUser);
                })
                .log()
                .flatMap(editedUser -> ok().contentType(APPLICATION_JSON).syncBody(editedUser))
                .switchIfEmpty(
                        badRequest()
                                .syncBody("Error: User does not exist and can not be edited."));
    }

    public Mono<ServerResponse> deleteUser(ServerRequest request) {
        String userId = request.pathVariable("id");
        if (!isValidId(userId)) return badRequest().syncBody("Error: Invalid URL user id: " + userId);

        Mono<ServerResponse> notFound = ServerResponse.notFound().build();
        return userRepo.findById(userId)
                .flatMap(user -> userRepo.deleteById(userId)
                        .doOnEach(signal -> log.debug("USER DELETED: " + user))
                        .then(ok().contentType(APPLICATION_JSON).syncBody(user)))
                .switchIfEmpty(notFound);
    }

    public boolean isValidId(String id) {
        return id != null && id.length() == 24;
    }
}
