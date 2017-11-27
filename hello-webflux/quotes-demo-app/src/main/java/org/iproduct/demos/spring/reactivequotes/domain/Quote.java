package org.iproduct.demos.spring.reactivequotes.domain;

import lombok.*;

import javax.validation.constraints.NotNull;
import java.time.Instant;

//import org.springframework.data.mongodb.core.mapping.Document;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
@Builder
public class Quote {

    private static long nextId = 0;

    @NotNull
    private String id;

    @NotNull
    private String symbol = "IPT";

    @NotNull
    private double price;

    private Instant instant  = Instant.now();

    public Quote(String symbol, double price){
        id = "" + nextId++;
        this.symbol = symbol;
        this.price = price;
    }

}
