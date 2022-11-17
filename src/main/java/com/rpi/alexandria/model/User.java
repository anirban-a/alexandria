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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

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

	private Boolean isVerified;

	private Boolean isAccountActive;

	private Map<String, Integer> ratings = new HashMap<>();	// key: email of user that provided rating, value: rating
	public double getAverageRating() {
		int count = 0;
		double total = 0.0;

		Iterator<Map.Entry<String, Integer>> ratingsItr = ratings.entrySet().iterator();

		while (ratingsItr.hasNext()) {
			count++;
			total += ratingsItr.next().getValue();
		}

		return total / count;
	}
}
