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

import java.util.*;

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

	private Map<String, Integer> ratings = new HashMap<>(); // key: username of user that
															// provided rating, value:
															// rating value

	private Set<String> usernamesRated = new HashSet<>();

	public boolean hasRated(String usernameOther) {
		return usernamesRated.contains(usernameOther);
	}

	public void addRating(String usernameOther, int ratingValue) {
		ratings.put(usernameOther, ratingValue);
	}

	public void addUsernameToUsernamesRated(String username) {
		usernamesRated.add(username);
	}

	public void updateRating(String usernameOther, int ratingValue) {
		ratings.put(usernameOther, ratingValue);
	}

	public void deleteRating(String usernameOther) {
		ratings.remove(usernameOther);
	}

	public void removeUsernameFromUsernamesRated(String username) {
		usernamesRated.remove(username);
	}

	public double getAverageRating() {
		int count = 0;
		double total = 0.0;

		Iterator<Map.Entry<String, Integer>> ratingsItr = ratings.entrySet().iterator();

		while (ratingsItr.hasNext()) {
			count++;
			total += ratingsItr.next().getValue();
		}

		if (count == 0)
			return 0.0;

		return total / count;
	}

}
