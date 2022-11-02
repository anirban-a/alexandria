package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Container(containerName = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

	@Id
	@PartitionKey
	@Field(type = FieldType.Keyword)
	private String username;

	private String firstName;

	private String lastName;

	private String address;

	private String primaryEmail;

	private String secondaryEmail;

	private University university;

	private String password;

	private String passwordResetToken;

	private Boolean isVerified;

	private Boolean isAccountActive;

}
