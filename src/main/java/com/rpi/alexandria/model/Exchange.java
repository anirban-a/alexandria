package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

import java.util.Objects;

@Container(containerName = "exchange")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Exchange implements IBookExchange {
    String id;

    @PartitionKey
    String firstPartyId;

    String otherPartyId;

    String firstPartyBookId;

    /**
     * @param id Id for the exchange
     * @apiNote Do not use this. Use <code>computeId()</code> instead.
     */
    public void setId(String id) {
        this.id = id;
    }

    String otherPartyBookId;

    Boolean completed = false;

    public Exchange deriveOtherPartyExchange() {
        Exchange otherPartyExchange = new Exchange();
        if(Objects.nonNull(id)){
            otherPartyExchange.setId(id.split("_")[0]+"_"+otherPartyId);
        }
        otherPartyExchange.setFirstPartyId(otherPartyId);
        otherPartyExchange.setOtherPartyId(firstPartyId);
        otherPartyExchange.setFirstPartyBookId(otherPartyBookId);
        otherPartyExchange.setOtherPartyBookId(firstPartyBookId);
        return otherPartyExchange;
    }

    public void computeId(){
        id = RandomStringUtils.random(5, false, true) + this.hashCode() + "_" + firstPartyId;
    }
}
