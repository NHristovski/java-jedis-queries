package io.yelp.project;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class RedisUtils {
    public static List<String> keys;
    private static Random random;

    static {
        String[] k = new String[]{
                 "user:QaELAmRcDc5TfJEylaaP8g"
                , "business:oiAlXZPIFm2nBCt0DHLu_Q:hours"
                , "business:f9NumwFMBDn751xgFiRbNA:hours"
                , "business:pQeaRpvuhoEqudo3uymHIQ:hours"
                , "business:Yzvjg0SayhoZgCljUJRF9Q"
                , "business:ScYkbYNkDgCneBrD9vqhCQ:hours"
                , "business:pQeaRpvuhoEqudo3uymHIQ"
        };
        keys = Arrays.asList(k);
        random = new Random();
    }

    public static String getRandomNonExistingKey() {
        byte[] array = new byte[64]; // length is bounded by 7
        random.nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }

    public static String getRandomValue() {
        byte[] array = new byte[64]; // length is bounded by 7
        random.nextBytes(array);

        return new String(array, StandardCharsets.UTF_8);
    }

    public static String getRandomExistingKey() {
        return keys.get(random.nextInt(keys.size()));
    }

    public static boolean shouldBeGetQuery() {
        return random.nextBoolean();
    }
//    public RedisKeys(){
//        String[] k = new String[]{
//                "a",
//                "b"
//        };
//         =
//    }

}
