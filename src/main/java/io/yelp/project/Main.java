package io.yelp.project;

import redis.clients.jedis.JedisCluster;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        var properties = new ApplicationProperties();

        JedisCluster cluster = new JedisCluster(properties.getHostsAndPorts(), 3000, 50);

        ExecutorService service = Executors.newFixedThreadPool(properties.getThreadsCount());

        List<Future<String>> futures = new ArrayList<>();

        System.out.println("Starting queries at: " + Instant.now());

        long start = System.currentTimeMillis();

        for (int i = 0; i < properties.getQueriesCount(); i++) {
            Future<String> futureResult = service.submit(createGetQuery(cluster));
            futures.add(futureResult);
        }

        futures.forEach(f -> {
            try {
                f.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        long end = System.currentTimeMillis();

        long milliseconds = end - start;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);

        System.out.format("%d Milliseconds = %d minutes\n", milliseconds, minutes );

        System.out.println("End all queries at: " + Instant.now());

        service.shutdown();
    }

    private static Callable<String> createGetQuery(JedisCluster cluster) {
        return () -> {
            if (RedisUtils.shouldBeRangeQuery()) {
                return createGetRangeQuery(cluster);
            }
            String randomExistingKey = RedisUtils.getRandomExistingKey();
//            System.out.println("Redis GET key: " + randomExistingKey);
            return cluster.hgetAll(randomExistingKey).toString();
        };
    }

    private static String createGetRangeQuery(JedisCluster cluster) {
        String randomExistingKey = RedisUtils.getRandomExistingKey();

        if (randomExistingKey.startsWith("business")) {
            randomExistingKey = randomExistingKey + ":reviews";
//            System.out.println("Redis RANGE key: " + randomExistingKey);

            return cluster.lrange(randomExistingKey, 0L, -1L).toString();
        } else if (randomExistingKey.startsWith("user")) {
            randomExistingKey = randomExistingKey + ":friends";
//            System.out.println("Redis RANGE key: " + randomExistingKey);

            return cluster.lrange(randomExistingKey, 0L, -1L).toString();
        } else {
            randomExistingKey = RedisUtils.keys.get(0) + ":reviews";
//            System.out.println("Redis RANGE key: " + randomExistingKey);
            return cluster.lrange(randomExistingKey, 0L, -1L).toString();
        }
    }

    private static Callable<String> createSetQuery(JedisCluster cluster) {
        return () -> {
            String randomNonExistingKey = RedisUtils.getRandomNonExistingKey();
            String randomValue = RedisUtils.getRandomValue();
//            System.out.println("Redis SET key: " + randomNonExistingKey + "  values: " + randomValue);
            return cluster.set(randomNonExistingKey, randomValue);
        };
    }

    private static Callable<String> createRandomQuery(JedisCluster cluster) {
        if (RedisUtils.shouldBeGetQuery()) {
            return createGetQuery(cluster);
        }
        return createSetQuery(cluster);
    }

}
