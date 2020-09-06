package io.yelp.project;

import redis.clients.jedis.JedisCluster;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

public class Main {

    public static void main(String[] args) {
        var properties = new ApplicationProperties();

        JedisCluster cluster = new JedisCluster(properties.getHostsAndPorts());

        ExecutorService service = Executors.newFixedThreadPool(properties.getThreadsCount());

        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < properties.getQueriesCount(); i++) {
            Future<String> futureResult = service.submit(createGetQuery(cluster));
            futures.add(futureResult);
        }

        System.out.println("Before for each");
        futures.forEach(f -> {
            try {
                System.out.println(f.get());
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        service.shutdown();
    }

    private static Callable<String> createGetQuery(JedisCluster cluster) {
        return () -> cluster.hgetAll(RedisUtils.getRandomExistingKey()).toString();
    }

    private static Callable<String> createSetQuery(JedisCluster cluster) {
        return () -> cluster.set(RedisUtils.getRandomNonExistingKey(), RedisUtils.getRandomValue());
    }

    private static Callable<String> createRandomQuery(JedisCluster cluster) {
        if (RedisUtils.shouldBeGetQuery()){
            return createGetQuery(cluster);
        }
        return createSetQuery(cluster);
    }

}
