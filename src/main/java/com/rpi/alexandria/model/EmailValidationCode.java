package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.Data;
import org.springframework.data.annotation.Id;

@Data
@Container(containerName = "email_validation_code")
public class EmailValidationCode {

	@Id
	@PartitionKey
	String userId;

	String validationCode;

}
