package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.ToString;

import javax.validation.constraints.NotBlank;

@Data
@ToString
@Container(containerName = "book")
public class Book {
    String id;
    @NotBlank(message = "ISBN is mandatory")
    @PartitionKey
    String isbn;
    @NotBlank(message = "Book name is mandatory")
    String name;
    String normalizedName; // internal property. This property will be used to search a book by name.
    @NotBlank(message = "Book condition is mandatory")
    Condition condition;
    String description;
    Boolean forExchange;
    Boolean forGiveAway;
    User listedBy; // internal property.

    public static String getNormalizedName(String name) {
        return String.join("_", name.toLowerCase().split(" "));
    }

    public void setName(String name) {
        this.name = name;
        this.normalizedName = getNormalizedName(name);
    }

    public enum Condition {
        @JsonProperty("As New")
        AS_NEW,
        @JsonProperty("Fine")
        FINE,
        @JsonProperty("Good")
        GOOD,
        @JsonProperty("Fair")
        FAIR,
        @JsonProperty("Nearly Damaged")
        NEARLY_DAMAGED,
        @JsonProperty("Damaged")
        DAMAGED
    }
}
