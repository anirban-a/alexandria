package com.rpi.alexandria.controller;

import com.nimbusds.jose.shaded.json.JSONObject;
import com.rpi.alexandria.controller.response.AppResponse;
import com.rpi.alexandria.dto.RatingDTO;
import com.rpi.alexandria.model.Rating;
import com.rpi.alexandria.model.User;
import com.rpi.alexandria.service.UserService;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.OffsetDateTime;
import java.util.*;

@RestController
@RequestMapping("/api/rating")
@Slf4j
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@CrossOrigin("http://localhost:${ui.port}")
public class RatingController extends BaseController {

	public RatingController(UserService userService) {
		super(userService);
	}

	@PostMapping("/add")
	public ResponseEntity<AppResponse> addRating(@RequestBody Rating rating) {
		log.info("Received request to add rating to user: {}", rating.getUsernameOther());

		int ratingValue = rating.getRatingValue();

		// Cannot add rating with value less than 1 or greater than 5
		if (ratingValue < 1 || ratingValue > 5) {
			AppResponse appResponse = new AppResponse(
					String.format("Invalid rating value %d. Allowed rating values are integer values from 1 to 5.",
							ratingValue),
					OffsetDateTime.now(), HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		User loggedInUser = getUser();
		String loggedInUserUsername = loggedInUser.getUsername();

		// Cannot add rating to same account
		if (loggedInUserUsername.equals(rating.getUsernameOther())) {
			AppResponse appResponse = new AppResponse("Cannot add rating to same account", OffsetDateTime.now(),
					HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		// Cannot add more than one rating to same user
		if (userService.hasAddedRating(loggedInUser, rating)) {
			AppResponse appResponse = new AppResponse(
					String.format("%s has already added rating to %s", loggedInUserUsername, rating.getUsernameOther()),
					OffsetDateTime.now(), HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		userService.addRating(loggedInUser, rating);

		AppResponse appResponse = buildAppResponse(String.format("%s successfully added rating of %d to %s",
				loggedInUserUsername, rating.getRatingValue(), rating.getUsernameOther()), HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@PutMapping("/update")
	public ResponseEntity<AppResponse> updateRating(@RequestBody Rating rating) {
		log.info("Received request to update rating of user: {}", rating.getUsernameOther());

		int ratingValue = rating.getRatingValue();

		// Cannot update rating with value less than 1 or greater than 5
		if (ratingValue < 1 || ratingValue > 5) {
			AppResponse appResponse = new AppResponse(
					String.format("Invalid rating value %d. Allowed rating values are integer values from 1 to 5.",
							ratingValue),
					OffsetDateTime.now(), HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		User loggedInUser = getUser();
		String loggedInUserUsername = loggedInUser.getUsername();

		// Cannot update rating to same account
		if (loggedInUserUsername.equals(rating.getUsernameOther())) {
			AppResponse appResponse = new AppResponse("Cannot update rating to same account", OffsetDateTime.now(),
					HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		// Cannot update rating if no rating has been created
		if (!userService.hasAddedRating(loggedInUser, rating)) {
			AppResponse appResponse = new AppResponse(
					String.format("%s has not added rating to %s, so cannot update the rating", loggedInUserUsername,
							rating.getUsernameOther()),
					OffsetDateTime.now(), HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		userService.updateRating(loggedInUserUsername, rating);
		AppResponse appResponse = buildAppResponse(String.format("%s successfully updated rating to %d for %s",
				loggedInUserUsername, rating.getRatingValue(), rating.getUsernameOther()), HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@GetMapping("/average")
	public ResponseEntity<AppResponse> getAverage(@RequestBody Rating rating) {
		log.info("Received request to get average rating of user: {}", rating.getUsernameOther());

		double averageRating = userService.getAverageRating(rating);
		JSONObject averageRatingsJSON = new JSONObject();
		averageRatingsJSON.put("averageRating", averageRating);

		AppResponse appResponse = new AppResponse<>(
				String.format("Retrieved average rating of user with username %s", rating.getUsernameOther()),
				OffsetDateTime.now(), HttpStatus.OK, "", averageRatingsJSON);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@GetMapping("/ratings")
	public ResponseEntity<AppResponse> getRatings(@RequestBody Rating rating) {
		log.info("Received request to get ratings from user: {}", rating.getUsernameOther());
		Map<String, Integer> ratings = userService.getRatings(rating);

		List<RatingDTO> ratingDTOList = new ArrayList<>();
		Iterator<Map.Entry<String, Integer>> ratingsItr = ratings.entrySet().iterator();
		while (ratingsItr.hasNext()) {
			Map.Entry ratingsInfo = ratingsItr.next();
			ratingDTOList.add(new RatingDTO((String) ratingsInfo.getKey(), (int) ratingsInfo.getValue()));
		}

		AppResponse<List<RatingDTO>> appResponse = new AppResponse<>(
				String.format("Retrieved ratings of user with username %s", rating.getUsernameOther()),
				OffsetDateTime.now(), HttpStatus.OK, "", ratingDTOList);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@GetMapping("/rated")
	public ResponseEntity<AppResponse> getUsernamesRated() {
		User loggedInUser = getUser();
		log.info("Received request to get usernames that user: {} has rated", loggedInUser.getUsername());

		Set<String> usernamesRated = userService.getUsernamesRated(loggedInUser);

		List<String> usernamesRatedList = new ArrayList<>();
		Iterator<String> usernamesRatedItr = usernamesRated.iterator();
		while (usernamesRatedItr.hasNext()) {
			String usernameRated = usernamesRatedItr.next();
			usernamesRatedList.add(usernameRated);
		}

		AppResponse<List<String>> appResponse = new AppResponse<>(
				String.format("Retrieved usernames that %s rated", loggedInUser.getUsername()), OffsetDateTime.now(),
				HttpStatus.OK, "", usernamesRatedList);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

	@DeleteMapping("/delete")
	public ResponseEntity<AppResponse> deleteRating(@RequestBody Rating rating) {
		log.info("Received request to remove rating from user: {}", rating.getUsernameOther());

		User loggedInUser = getUser();
		String loggedInUserUsername = loggedInUser.getUsername();

		// Cannot delete rating to same account
		if (loggedInUserUsername.equals(rating.getUsernameOther())) {
			AppResponse appResponse = new AppResponse("Cannot delete rating to same account", OffsetDateTime.now(),
					HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		// Cannot delete rating if no rating has been created
		if (!userService.hasAddedRating(loggedInUser, rating)) {
			AppResponse appResponse = new AppResponse(
					String.format("%s has not added rating to %s, so cannot delete the rating", loggedInUserUsername,
							rating.getUsernameOther()),
					OffsetDateTime.now(), HttpStatus.BAD_REQUEST, "", null);
			return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
		}

		userService.deleteRating(loggedInUser, rating);
		AppResponse appResponse = buildAppResponse(
				String.format("%s successfully deleted rating for %s", loggedInUserUsername, rating.getUsernameOther()),
				HttpStatus.OK);
		return new ResponseEntity<>(appResponse, appResponse.getHttpStatus());
	}

}
