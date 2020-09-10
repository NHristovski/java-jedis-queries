package io.yelp.project;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisCluster;
import redis.clients.jedis.ScanParams;
import redis.clients.jedis.ScanResult;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;

import static redis.clients.jedis.ScanParams.SCAN_POINTER_START;

public class Main {

    public static void main(String[] args) throws ExecutionException, InterruptedException {

        var properties = new ApplicationProperties();


        JedisCluster cluster = new JedisCluster(properties.getHostsAndPorts(), 3000, 50);
        ScanParams scanParams = new ScanParams();

//        System.out.println(cluster.scan("0", scanParams.count(200).match("*")).getResult());

        String keyPrefix = "business:*";
//        String keyPrefix1 = "user:*";
//        String keyPrefix2 = "review:*";

        ScanParams params = new ScanParams()
                .match(keyPrefix)
                .count(100_000);

        cluster.getClusterNodes().values().stream().forEach(pool -> {
            boolean done = false;
            String cur = SCAN_POINTER_START;
            try (Jedis jedisNode = pool.getResource()) {
                while (!done) {
                    ScanResult<String> resp = jedisNode.scan(cur, params);

                    StringBuilder builder = new StringBuilder();
                    for (String s : resp.getResult()) {
                        builder.append(s).append("\n");
                    }

                    try {
                        Files.write(Paths.get("business_keys.txt"), builder.toString().getBytes(), StandardOpenOption.CREATE);
                    } catch (IOException e) {
                        //exception handling left as an exercise for the reader
                        e.printStackTrace();
                    }

                    cur = resp.getCursor();
                    if (cur.equals(SCAN_POINTER_START)) {
                        done = true;
                    }
                }
            }
    });
//        ExecutorService service = Executors.newFixedThreadPool(properties.getThreadsCount());
//
//        List<Future<String>> futures = new ArrayList<>();
//
//        System.out.println("Starting queries at: " + Instant.now());
//
//        long start = System.currentTimeMillis();
//
//        for (int i = 0; i < properties.getQueriesCount(); i++) {
//            Future<String> futureResult = service.submit(createGetQuery(cluster));
//            futures.add(futureResult);
//        }
//
//        futures.forEach(f -> {
//            try {
//                f.get();
//            } catch (InterruptedException | ExecutionException e) {
//                e.printStackTrace();
//            }
//        });
//
//        long end = System.currentTimeMillis();
//
//        long milliseconds = end - start;
//
//        long minutes = TimeUnit.MILLISECONDS.toMinutes(milliseconds);
//
//        System.out.format("%d Milliseconds = %d minutes\n", milliseconds, minutes );
//
//        System.out.println("End all queries at: " + Instant.now());
//
//        service.shutdown();
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
