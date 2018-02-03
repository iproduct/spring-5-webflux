package org.iproduct.demos.spring.streamingdemos.services;

import org.iproduct.demos.spring.streamingdemos.domain.Quote;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class QuotesGenerator {

    private Random random = new Random();

    private List<Quote> quotes = Arrays.asList(
            new Quote("VMW", 215.35),
            new Quote("GOOG", 309.17),
            new Quote("CTXS", 112.11),
            new Quote("DELL", 92.23),
            new Quote("MSFT", 75.19),
            new Quote("ORCL", 115.72),
            new Quote("RHT", 111.19)

    );


    public Flux<Quote> getQuoteStream(Duration period) {
        return Flux.interval(period)
                .map(index -> {
                    Quote quote = quotes.get(index.intValue() % quotes.size());
                    quote.setPrice(quote.getPrice() * (0.95 + 0.1 * Math.random()));
                    return new Quote(quote.getSymbol(), quote.getPrice());
                })
                .share()
                .log();
    }
}
