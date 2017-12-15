package org.iproduct.demos.spring.client;

import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;


public class StopApplication {

    public static void main(String[] args) throws IOException, InterruptedException {
        Properties prop = new Properties();
        InputStream input = null;
        String filename = "application.properties";
        input = StopApplication.class.getClassLoader().getResourceAsStream(filename);
        prop.load(input);

        String baseUrl = "http://localhost:" + prop.getProperty("server.port");
        WebClient client = WebClient.builder().baseUrl(baseUrl).build();
        System.out.println("Stopper web client constructed for user service @ " + baseUrl);
        client
                .mutate()
                .filter(adminCredentials())
                .build()
                .post()
                .uri("/shutdown")
                .contentType(MediaType.APPLICATION_JSON)
                .syncBody("{}")
                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .flatMap(response -> response.bodyToMono(String.class))
                .subscribe(user -> System.out.println("Users service stopped with result: " +  user),
                        err -> System.out.println("Error stopping user service: " +  err),
                        () -> System.out.println("Client complete."));

        Thread.sleep(10000);
    }

    private static ExchangeFilterFunction adminCredentials() {
        return basicAuthentication("admin", "admin");
    }
}
