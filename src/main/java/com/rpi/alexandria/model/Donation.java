package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.RandomStringUtils;

@Container(containerName = "donation")
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Donation implements IBookDonation {

	String id;

	@PartitionKey
	String firstPartyId;

	String otherPartyId;

	String firstPartyBookId;

	Boolean completed = false;

	public void computeId() {
		id = RandomStringUtils.random(5, false, true) + this.hashCode() + "_" + firstPartyId;
	}
}
