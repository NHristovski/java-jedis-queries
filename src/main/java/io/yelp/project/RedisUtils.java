package io.yelp.project;

import java.util.List;
import java.util.Random;
import java.util.UUID;

public class RedisUtils {
	private static final Random random = new Random();

	private static final List<String> keys = List.of(
			"user:QaELAmRcDc5TfJEylaaP8g",
			"business:oiAlXZPIFm2nBCt0DHLu_Q:hours",
			"business:f9NumwFMBDn751xgFiRbNA:hours",
			"business:pQeaRpvuhoEqudo3uymHIQ:hours",
			"business:Yzvjg0SayhoZgCljUJRF9Q",
			"business:ScYkbYNkDgCneBrD9vqhCQ:hours",
			"business:pQeaRpvuhoEqudo3uymHIQ"
	);

	public static String getRandomNonExistingKey() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomValue() {
		return UUID.randomUUID().toString();
	}

	public static String getRandomExistingKey() {
		return keys.get(random.nextInt(keys.size()));
	}

	public static boolean shouldBeGetQuery() {
		return random.nextBoolean();
	}
}
