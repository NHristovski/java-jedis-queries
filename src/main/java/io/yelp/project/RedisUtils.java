package io.yelp.project;

import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class RedisUtils {
	private static final Random random = new Random();

	public static String getRandomNonExistingKey() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomValue() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomBusinessKey() throws IOException {
		var businessesIds = IdsReader.readBusinessesIds();
		var businessIdIndex = random.nextInt(businessesIds.size());
		return businessesIds.get(businessIdIndex);
	}

	private static String getRandomReviewKey() throws IOException {
		var reviewsIds = IdsReader.readReviewsIds();
		var reviewIdIndex = random.nextInt(reviewsIds.size());
		return reviewsIds.get(reviewIdIndex);
	}

	private static String getRandomUserKey() throws IOException {
		var usersIds = IdsReader.readUsersIds();
		var userIdIndex = random.nextInt(usersIds.size());
		return usersIds.get(userIdIndex);
	}

	public static String getRandomHGetAllKey() throws IOException {
		var entityIndex = random.nextInt(4);
		if (entityIndex == 0) {
			return String.format("business:%s", getRandomBusinessKey());
		} else if (entityIndex == 1) {
			return String.format("business:%s:hours", getRandomBusinessKey());
		} else if (entityIndex == 2) {
			return String.format("review:%s", getRandomReviewKey());
		} else {
			return String.format("user:%s", getRandomUserKey());
		}
	}

	public static String getRandomHGetAllKeyModelTwo() throws IOException {
		var entityIndex = random.nextInt(4);
		if (entityIndex == 0) {
			return String.format("business:%s", getRandomBusinessKey());
		} else if (entityIndex == 1) {
			return String.format("business:%s:hours", getRandomBusinessKey());
		} else if (entityIndex == 2) {

			String reviewFullKey = getRandomReviewKey();

			String reviewKey = reviewFullKey.split(" ")[0];
			String businessKey = reviewFullKey.split(" ")[1];

			return String.format("business:%s:review:%s", businessKey, reviewKey);
		} else {
			return String.format("user:%s", getRandomUserKey());
		}
	}

	public static String getRandomLRangeKey() throws IOException {
		var entityIndex = random.nextInt(4);
		if (entityIndex == 0) {
			return String.format("business:%s:categories", getRandomBusinessKey());
		} else if (entityIndex == 1) {
			return String.format("business:%s:reviews", getRandomBusinessKey());
		} else if (entityIndex == 2) {
			return String.format("user:%s:friends", getRandomUserKey());
		} else {
			return String.format("user:%s:reviews", getRandomUserKey());
		}
	}

	public static String getRandomLRangeKeyModelTwo() throws IOException {
		if (random.nextBoolean()){
			return String.format("business:%s:categories", getRandomBusinessKey());
		}
		return String.format("user:%s:friends", getRandomUserKey());
	}

	public static boolean shouldBeGetQuery() {
		return random.nextBoolean();
	}

	public static boolean shouldBeRangeQuery() {
		return random.nextBoolean();
	}
}
