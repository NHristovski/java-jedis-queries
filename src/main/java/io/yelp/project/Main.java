package io.yelp.project;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class Main {

    public static void main(String[] args) {
        int N_THREADS = 64;
        int NUMBER_OF_QUERIES = 10;

        Properties properties = new Properties();

        try {
            properties.load(Main.class.getResourceAsStream("/application.properties"));

        } catch (IOException e) {
            e.printStackTrace();
        }

        Set<HostAndPort> set = new HashSet<>();

        set.add(new HostAndPort(properties.getProperty("redis.cluster.master1.host"),
                Integer.parseInt(properties.getProperty("redis.cluster.master1.port"))));
        set.add(new HostAndPort(properties.getProperty("redis.cluster.slave1.host"),
                Integer.parseInt(properties.getProperty("redis.cluster.slave1.port"))));
        set.add(new HostAndPort(properties.getProperty("redis.cluster.master2.host"),
                Integer.parseInt(properties.getProperty("redis.cluster.master2.port"))));
        set.add(new HostAndPort(properties.getProperty("redis.cluster.slave2.host"),
                Integer.parseInt(properties.getProperty("redis.cluster.slave2.port"))));
        set.add(new HostAndPort(properties.getProperty("redis.cluster.master3.host"),
                Integer.parseInt(properties.getProperty("redis.cluster.master3.port"))));
        set.add(new HostAndPort(properties.getProperty("redis.cluster.slave3.host"),
                Integer.parseInt(properties.getProperty("redis.cluster.slave3.port"))));


        N_THREADS = Integer.parseInt(properties.getProperty("threads"));
        NUMBER_OF_QUERIES = Integer.parseInt(properties.getProperty("num_of_queries"));

        JedisCluster cluster = new JedisCluster(set);

        ExecutorService service = Executors.newFixedThreadPool(N_THREADS);

        List<Future<String>> futures = new ArrayList<>();

        for (int i = 0; i < NUMBER_OF_QUERIES; i++) {
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
//        cluster.hget("business")
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
