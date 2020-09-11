package io.yelp.project.memcached;

import io.yelp.project.ApplicationProperties;
import io.yelp.project.RedisUtils;

import java.io.IOException;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

public class Main {
    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        runMemcachedQueriesModelTwo(args);
    }

    public static void runMemcachedQueriesModelTwo(String[] args) throws IOException {
        String configFile = args[0];

        System.out.println("The config file is: " + configFile);

        ApplicationProperties properties = new ApplicationProperties(configFile);

        MemcachedFacade.init(properties);

        ExecutorService service = Executors.newFixedThreadPool(properties.getThreadsCount());

        List<java.util.concurrent.Future<String>> futures = new ArrayList<>();

        System.out.println("Starting queries at: " + Instant.now());

        long start = System.currentTimeMillis();

        for (int i = 0; i < properties.getQueriesCount(); i++) {
            Future<String> futureResult = service.submit(createGetQueryForMemcachedModelTwo());
            futures.add(futureResult);
        }

        for (int i = 0; i < futures.size(); i++) {
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

        System.out.format("%d Milliseconds = %d minutes\n", milliseconds, minutes);

        System.out.println("End all queries at: " + Instant.now());

        service.shutdown();
    }

    private static Callable<String> createGetQueryForMemcachedModelTwo() throws IOException {
        return () -> {
            String randomKey = RedisUtils.getRandomMemcachedKeyModelTwo();

            return MemcachedFacade.get(randomKey);
        };
    }

    public static void runMemcachedQueriesModelOne(String[] args) throws IOException, ExecutionException, InterruptedException {
        String configFile = args[0];

        System.out.println("The config file is: " + configFile);

        ApplicationProperties properties = new ApplicationProperties(configFile);

        MemcachedFacade.init(properties);

        ExecutorService service = Executors.newFixedThreadPool(properties.getThreadsCount());

        List<java.util.concurrent.Future<String>> futures = new ArrayList<>();

        System.out.println("Starting queries at: " + Instant.now());

        long start = System.currentTimeMillis();

        for (int i = 0; i < properties.getQueriesCount(); i++) {
            Future<String> futureResult = service.submit(createGetQueryForMemcachedModelOne());
            futures.add(futureResult);
        }

        for (int i = 0; i < futures.size(); i++) {
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

        System.out.format("%d Milliseconds = %d minutes\n", milliseconds, minutes);

        System.out.println("End all queries at: " + Instant.now());

        service.shutdown();
    }

    private static Callable<String> createGetQueryForMemcachedModelOne() throws IOException {
        return () -> {
            String randomKey = RedisUtils.getRandomMemcachedKey();
            return MemcachedFacade.get(randomKey);
        };
    }
}


