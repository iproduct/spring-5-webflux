package org.iproduct.demos.spring.streamingdemos.domain;

import javax.validation.constraints.NotNull;
import java.time.Instant;

//import org.springframework.data.mongodb.core.mapping.Document;

//@Document(collection = "quotes")
//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//@EqualsAndHashCode(of = {"id"})
//@ToString
public class Quote {

    private static long nextId = 0;

//    @Id
    private String id;

    @NotNull
    private String symbol = "IPT";

    @NotNull
    private double price;

    private Instant instant  = Instant.now();

    public Quote(){
        id = "" + nextId++;
    }

    public Quote(String symbol, double price){
        id = "" + nextId++;
        this.symbol = symbol;
        this.price = price;
        instant = Instant.now();
    }

    public Quote(String id, @NotNull String symbol, @NotNull double price, Instant instant) {
        this.id = id;
        this.symbol = symbol;
        this.price = price;
        this.instant = instant;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Instant getInstant() {
        return instant;
    }

    public void setInstant(Instant instant) {
        this.instant = instant;
    }

    @Override
    public String toString() {
        return "Quote{" +
                "id='" + id + '\'' +
                ", symbol='" + symbol + '\'' +
                ", price=" + price +
                ", instant=" + instant +
                '}';
    }
}
