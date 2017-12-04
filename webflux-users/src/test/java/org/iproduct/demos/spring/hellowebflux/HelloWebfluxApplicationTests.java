package org.iproduct.demos.spring.hellowebflux;

import lombok.extern.slf4j.Slf4j;
import org.iproduct.demos.spring.hellowebflux.domain.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = HelloWebfluxApplication.class)
@Slf4j
//@TestPropertySource(properties = {"local.server.port=9000", "local.management.port=0"})
public class HelloWebfluxApplicationTests {

    @Autowired
    private ApplicationContext context;

    private WebTestClient client;
    private User createdCustomer;

    @Before
    public void setUp() {
        client = WebTestClient.bindToApplicationContext(context).build();
        client
                .mutate()
                .filter(rootCredentials())
                .build()
                .post()
                .uri("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody("{\"username\":\"testUser\", \"password\":\"test123\", \"fname\":\"Spring\",\"lname\":\"User\", \"role\":\"CUSTOMER\"}")
                .exchange()
                .expectStatus().isCreated()
                .returnResult(User.class)
                .consumeWith(userRespFlux -> {
//                    log.info(userRespFlux.toString());
                    userRespFlux.getResponseBody()
                            .subscribe((User user) -> {
                                createdCustomer = user;
                            });
                });
    }

    @After
    public void cleanUpAfterClass() {
        client
                .mutate()
                .filter(rootCredentials())
                .build()
                .delete()
                .uri("/api/users/" + createdCustomer.getId())
                .exchange()
                .returnResult(User.class)
                .consumeWith(user -> {
//                    log.info(user.toString());
                });
    }

    @Test
    public void basicRequired() throws Exception {
        client
                .get()
                .uri("/api/users")
                .exchange()
                .expectStatus().isUnauthorized();
    }

    @Test
    public void basicWorks() throws Exception {
        client
                .mutate()
                .filter(rootCredentials())
                .build()
                .get()
                .uri("/users")
                .exchange()
                .expectStatus().isOk();
    }

//	@Test
//	public void principal() throws Exception {
//		this.rest
//				.filter(robsCredentials())
//				.get()
//				.uri("/principal")
//				.exchange()
//				.expectStatus().isOk()
//				.expectBody().json("{\"username\" : \"rob\"}");
//	}
//
//	@Test
//	public void headers() throws Exception {
//		this.rest
//				.filter(robsCredentials())
//				.get()
//				.uri("/principal")
//				.exchange()
//				.expectHeader().valueEquals(HttpHeaders.CACHE_CONTROL, "no-cache, no-store, max-age=0, must-revalidate")
//				.expectHeader().valueEquals(HttpHeaders.EXPIRES, "0")
//				.expectHeader().valueEquals(HttpHeaders.PRAGMA, "no-cache")
//				.expectHeader().valueEquals(ContentTypeOptionsHttpHeadersWriter.X_CONTENT_OPTIONS, ContentTypeOptionsHttpHeadersWriter.NOSNIFF);
//	}

    private static ExchangeFilterFunction rootCredentials() {
        return basicAuthentication("root", "root");
    }

    private static ExchangeFilterFunction adminCredentials() {
        return basicAuthentication("admin", "admin");
    }

}
