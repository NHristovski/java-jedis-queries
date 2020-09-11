package io.yelp.project;

import redis.clients.jedis.JedisCluster;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        runForRedisModelTwo(args);
    }

    private static void runForRedisModelTwo(String[] args){
        String configFile = args[0];

        System.out.println("The config file is: " + configFile);

        ApplicationProperties properties = new ApplicationProperties(configFile);

        System.out.println("Application properties: " + properties.toString());

        JedisCluster cluster = new JedisCluster(properties.getHostsAndPorts(), 3000, 50);

        ExecutorService service = Executors.newFixedThreadPool(properties.getThreadsCount());

        List<Future<String>> futures = new ArrayList<>();

        System.out.println("Starting queries at: " + Instant.now());

        long start = System.currentTimeMillis();

        for (int i = 0; i < properties.getQueriesCount(); i++) {
            Future<String> futureResult = service.submit(createGetQueryForRedisModelTwo(cluster));
            futures.add(futureResult);
        }

        for (int i = 0; i < futures.size(); i++){
            try {
                futures.get(i).get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            futures.set(i, null);
        }

        long end = System.currentTimeMillis();

        long milliseconds = end - start;

        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);

        System.out.format("%d Milliseconds = %d minutes\n", milliseconds, minutes );

        System.out.println("End all queries at: " + Instant.now());

        service.shutdown();
    }

    private static void runForRedisModelOne(String[] args){
        String configFile = args[0];

        System.out.println("The config file is: " + configFile);

        ApplicationProperties properties = new ApplicationProperties(configFile);

        System.out.println("Application properties: " + properties.toString());

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

    private static Callable<String> createGetQueryForRedisModelTwo(JedisCluster cluster){
        return () -> {
            if (RedisUtils.shouldBeRangeQuery()) {
                var start = 0;
                var stop = -1;
                var randomLRangeKey = RedisUtils.getRandomLRangeKeyModelTwo();
                var result = cluster.lrange(randomLRangeKey, start, stop).toString();
                return String.format("lrange %s %d %d => %s", randomLRangeKey, start, stop, result);
            }

            var randomHGetAllKey = RedisUtils.getRandomHGetAllKeyModelTwo();
            var result = cluster.hgetAll(randomHGetAllKey).toString();
            return String.format("hgetall %s => %s", randomHGetAllKey, result);
        };
    }


    private static Callable<String> createGetQuery(JedisCluster cluster) {
        return () -> {
            if (RedisUtils.shouldBeRangeQuery()) {
                var start = 0;
                var stop = -1;
                var randomLRangeKey = RedisUtils.getRandomLRangeKey();
                var result = cluster.lrange(randomLRangeKey, start, stop).toString();
                return String.format("lrange %s %d %d => %s", randomLRangeKey, start, stop, result);
            }
            var randomHGetAllKey = RedisUtils.getRandomHGetAllKey();
            var result = cluster.hgetAll(randomHGetAllKey).toString();
            return String.format("hgetall %s => %s", randomHGetAllKey, result);
        };
    }

    private static Callable<String> createSetQuery(JedisCluster cluster) {
        return () -> {
            var randomNonExistingKey = RedisUtils.getRandomNonExistingKey();
            var randomValue = RedisUtils.getRandomValue();
            var result = cluster.set(randomNonExistingKey, randomValue);
            return String.format("set %s %s => %s", randomNonExistingKey, randomValue, result);
        };
    }

    private static Callable<String> createRandomQuery(JedisCluster cluster) {
        if (RedisUtils.shouldBeGetQuery()) {
            return createGetQuery(cluster);
        }
        return createSetQuery(cluster);
    }

}
