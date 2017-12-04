package org.iproduct.demos.spring.reactivequotes.domain;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.NotNull;
import java.time.Instant;

//import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "quotes")
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = {"id"})
@ToString
public class Quote {

    private static long nextId = 0;

    @Id
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
