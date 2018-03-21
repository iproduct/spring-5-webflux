package webflux.demo.articles;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

interface ArticleRepository extends ReactiveMongoRepository<Article, String> {
}