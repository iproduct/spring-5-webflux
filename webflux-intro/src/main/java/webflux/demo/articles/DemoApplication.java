package webflux.demo.articles;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@SpringBootApplication
@EnableWebFlux
public class DemoApplication {

	public static void main(String[] args) {
		SpringApplication.run(DemoApplication.class, args);
	}

    @Bean
    public RouterFunction<ServerResponse> routes(ArticleHandler postController) {
        return route(GET("/articles"), postController::all)
                .andRoute(POST("/articles"), postController::create)
                .andRoute(GET("/articles/{id}"), postController::get)
                .andRoute(PUT("/articles/{id}"), postController::update)
                .andRoute(DELETE("/articles/{id}"), postController::delete);
    }
}
