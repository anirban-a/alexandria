package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

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
  Boolean completed = false;
  String initiatorId;

  /**
   * @param id Id for the exchange
   * @apiNote Do not use this. Use <code>computeId()</code> instead.
   */
  public void setId(String id) {
    this.id = id;
  }

  public Exchange deriveOtherPartyExchange() {
    Exchange otherPartyExchange = new Exchange();
    if (Objects.nonNull(id)) {
      otherPartyExchange.setId(deriveOtherPartyExchangeId());
    }
    otherPartyExchange.setFirstPartyId(otherPartyId);
    otherPartyExchange.setOtherPartyId(firstPartyId);
    otherPartyExchange.setFirstPartyBookId(otherPartyBookId);
    otherPartyExchange.setOtherPartyBookId(firstPartyBookId);
    otherPartyExchange.setInitiatorId(initiatorId);
    return otherPartyExchange;
  }

  public String deriveOtherPartyExchangeId() {
    return id.split("_")[0] + "_" + otherPartyId;
  }

  public void computeId() {
    id = RandomStringUtils.random(5, false, true) + this.hashCode() + "_" + firstPartyId;
  }

}
