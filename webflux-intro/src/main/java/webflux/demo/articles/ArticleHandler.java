package webflux.demo.articles;

import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

import java.net.URI;

@Component
class ArticleHandler {

    private final ArticleRepository repository;

    public ArticleHandler(ArticleRepository repository) {
        this.repository = repository;
    }

    public Mono<ServerResponse> all(ServerRequest req) {
        return ServerResponse.ok().body(this.repository.findAll(), Article.class);
    }

    public Mono<ServerResponse> create(ServerRequest req) {
        return req.bodyToMono(Article.class)
                .flatMap(article -> this.repository.save(article))
                .flatMap(p -> ServerResponse.created(URI.create("/repository/" + p.getId())).build());
    }

    public Mono<ServerResponse> get(ServerRequest req) {
        return this.repository.findById(req.pathVariable("id"))
                .flatMap(article -> ServerResponse.ok().body(Mono.just(article), Article.class))
                .switchIfEmpty(ServerResponse.notFound().build());
    }

    public Mono<ServerResponse> update(ServerRequest req) {

        return Mono
                .zip(
                        (tuple) -> {
                            Article oldArticle = (Article) tuple[0];
                            Article newArticle = (Article) tuple[1];
                            oldArticle.setTitle(newArticle.getTitle());
                            oldArticle.setContent(newArticle.getContent());
                            return oldArticle;
                        },
                        this.repository.findById(req.pathVariable("id")),
                        req.bodyToMono(Article.class)
                )
                .flatMap(this.repository::save)
                .flatMap(article -> ServerResponse.noContent().build());

    }

    public Mono<ServerResponse> delete(ServerRequest req) {
        return ServerResponse.noContent().build(this.repository.deleteById(req.pathVariable("id")));
    }

}