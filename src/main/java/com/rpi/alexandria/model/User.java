package com.rpi.alexandria.model;

import com.azure.spring.data.cosmos.core.mapping.Container;
import com.azure.spring.data.cosmos.core.mapping.PartitionKey;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.data.annotation.Id;

@Container(containerName = "user")
@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class User {

	@Id
	@PartitionKey
	private String username;

	private String firstName;

	private String lastName;

	private String address;

	private String primaryEmail;

	private String secondaryEmail;

	private University university;

	private String password;

}
