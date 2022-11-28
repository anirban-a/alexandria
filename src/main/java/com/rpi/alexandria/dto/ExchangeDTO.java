package com.rpi.alexandria.dto;

import com.rpi.alexandria.model.Exchange;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ExchangeDTO {

	String id;

	String firstPartyId;

	String otherPartyId;

	String firstPartyBookId;

	String otherPartyBookId;

	BookDTO firstPartyBookDetails;

	BookDTO otherPartyBookDetails;

	Boolean completed = false;

	String initiatorId;

	public static ExchangeDTO of(Exchange exchange) {
		ExchangeDTO exchangeDTO = new ExchangeDTO();
		exchangeDTO.setId(exchange.getId());
		exchangeDTO.setFirstPartyId(exchange.getFirstPartyId());
		exchangeDTO.setOtherPartyId(exchange.getOtherPartyId());
		exchangeDTO.setFirstPartyBookId(exchange.getFirstPartyBookId());
		exchangeDTO.setOtherPartyBookId(exchange.getOtherPartyBookId());
		exchangeDTO.setCompleted(exchange.getCompleted());
		exchangeDTO.setInitiatorId(exchange.getInitiatorId());

		return exchangeDTO;
	}

}
