package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import lombok.Data;
import lombok.ToString;

import java.util.List;

@Data
@ToString
@Container(containerName = "university")
public class University {

    String id;

    String name;

    List<String> domains;

    String country;

    public String computeId() {
        return String.join("_", name.toLowerCase().split(" "));
    }

}
