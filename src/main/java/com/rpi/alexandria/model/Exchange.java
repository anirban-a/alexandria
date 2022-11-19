package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Container(containerName = "exchange")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exchange implements IBookExchange {
    String id;

    @PartitionKey
    String firstPartyId;

    String otherPartyId;

    String firstPartyBookId;

    String otherPartyBookId;
}
