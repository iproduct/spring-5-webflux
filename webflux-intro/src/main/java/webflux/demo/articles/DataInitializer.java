package webflux.demo.articles;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

@Component
@Slf4j
class DataInitializer implements CommandLineRunner {

    private final ArticleRepository articles;

    public DataInitializer(ArticleRepository articles) {
        this.articles = articles;
    }

    @Override
    public void run(String[] args) {
        log.info("start data initialization  ...");
        this.articles
                .deleteAll()
                .thenMany(
                        Flux
                        .just("Article one", "Article two")
                        .flatMap(
                                title -> this.articles.save(Article.builder().title(title).content("content of " + title).build())
                        )
                )
                .log()
                .subscribe(
                        null,
                        null,
                        () -> log.info("done initialization...")
                );

    }

}