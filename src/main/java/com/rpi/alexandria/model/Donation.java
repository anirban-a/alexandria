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
	String firstPartyId; // id of the person who listed the book for donation.

	String otherPartyId; // id of the person requesting to acquire the book listed for
							// donation.

	String firstPartyBookId; // id of the book listed for donation.

	Boolean completed = false;

	public void computeId() {
		id = RandomStringUtils.random(5, false, true) + this.hashCode() + "_" + firstPartyId;
	}

}
