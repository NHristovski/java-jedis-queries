package io.yelp.project;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class IdsReader {

	private static final String PARENT = "/ids";

	private static final String BUSINESS_PARENT = PARENT + "/business";
	private static final String USER_PARENT = PARENT + "/user";
	private static final String REVIEW_PARENT = PARENT + "/review";

	private static final int BUSINESS_IDS_COUNT = 209_393;
	private static final int REVIEW_IDS_COUNT = 1_215_836;
	private static final int USER_IDS_COUNT = 75_728;

	private static final int BUSINESS_FILES_COUNT = 2;
	private static final int REVIEW_FILES_COUNT = 33;
	private static final int USER_FILES_COUNT = 14;

	private static List<String> businessesIdsCache;
	private static List<String> reviewIdsCache;
	private static List<String> usersIdsCache;

	public static List<String> readBusinessesIds() throws IOException {
		if (businessesIdsCache == null) {
			businessesIdsCache = readIds(BUSINESS_IDS_COUNT, BUSINESS_FILES_COUNT, BUSINESS_PARENT);
		}
		return businessesIdsCache;
	}

	public static List<String> readReviewsIds() throws IOException {
		if (reviewIdsCache == null) {
			reviewIdsCache = readIds(REVIEW_IDS_COUNT, REVIEW_FILES_COUNT, REVIEW_PARENT);
		}
		return reviewIdsCache;
	}

	public static List<String> readUsersIds() throws IOException {
		if (usersIdsCache == null) {
			usersIdsCache = readIds(USER_IDS_COUNT, USER_FILES_COUNT, USER_PARENT);
		}
		return usersIdsCache;
	}

	private static List<String> readIds(int idsCount, int filesCount, String filesParent) throws IOException {
		var ids = new ArrayList<String>(idsCount);
		for (int i = 0; i < filesCount; i++) {
			var path = String.format("%s/%d.txt", filesParent, i);
			ids.addAll(readFrom(path));
		}
		return ids;
	}

	private static List<String> readFrom(String path) throws IOException {
		try (var resource = IdsReader.class.getResourceAsStream(path)) {
			var reader = new BufferedReader(new InputStreamReader(resource, StandardCharsets.UTF_8));
			return reader.lines().collect(Collectors.toList());
		}
	}
}
