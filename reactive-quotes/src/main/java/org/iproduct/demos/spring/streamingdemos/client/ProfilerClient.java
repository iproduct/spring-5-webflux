package org.iproduct.demos.spring.streamingdemos.client;

import org.iproduct.demos.spring.streamingdemos.domain.CpuLoad;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.ExchangeFilterFunction;
import org.springframework.web.reactive.function.client.WebClient;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static org.springframework.web.reactive.function.client.ExchangeFilterFunctions.basicAuthentication;


public class ProfilerClient {

    public static void main(String[] args) throws IOException, InterruptedException {
        Properties prop = new Properties();
        InputStream input = null;
        String filename = "application.properties";
        input = ProfilerClient.class.getClassLoader().getResourceAsStream(filename);
        prop.load(input);

        String baseUrl = "http://localhost:" + prop.getProperty("server.port");
        WebClient client = WebClient.builder().baseUrl(baseUrl).build();
        System.out.println("ProfilerClient constructed @ " + baseUrl);
        client.get()
              .uri("/api/processes")
              .accept(MediaType.APPLICATION_JSON)
              .exchange()
              .flatMap(response -> response.bodyToMono(String.class))
              .subscribe(processes -> System.out.println("Active java processes: " +  processes),
                        err -> System.out.println("Error stopping user service: " +  err),
                        () -> System.out.println("Client complete."));

        client.get()
                .uri("/api/cpu")
                .accept(MediaType.APPLICATION_STREAM_JSON)
                .exchange()
                .flatMapMany(response -> response.bodyToFlux(CpuLoad.class))
                .subscribe(cpu -> System.out.println("Process CPU time: " +  cpu),
                        err -> System.out.println("Error stopping user service: " +  err),
                        () -> System.out.println("Client complete."));

//        client.get()
//                .uri("/api/quotes")
//                .accept(MediaType.APPLICATION_STREAM_JSON)
//                .exchange()
//                .flatMapMany(response -> response.bodyToFlux(Quote.class))
//                .subscribe(quote -> System.out.println("Active java processes: " +  quote),
//                        err -> System.out.println("Error stopping user service: " +  err),
//                        () -> System.out.println("Client complete."));

        Thread.sleep(10000);
    }

    private static ExchangeFilterFunction adminCredentials() {
        return basicAuthentication("admin", "admin");
    }
}
