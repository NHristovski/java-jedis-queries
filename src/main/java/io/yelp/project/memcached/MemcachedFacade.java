package io.yelp.project.memcached;

import io.yelp.project.ApplicationProperties;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.DefaultConnectionFactory;
import scala.Option;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.FiniteDuration;
import shade.memcached.Codec;
import shade.memcached.Configuration;
import shade.memcached.FailureMode;
import shade.memcached.Memcached;
import shade.memcached.Protocol;

import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MemcachedFacade {

    private static Memcached memcached;
    private static MyCodec codec;

    public static void init(ApplicationProperties applicationProperties) {
        Memcached memcached = Memcached.apply(new Configuration(applicationProperties.getMemcachedHosts(),
                Option.apply(null),
                Option.apply(null),
                Protocol.Binary(),
                FailureMode.Retry(),
                FiniteDuration.apply(1, TimeUnit.SECONDS),
                Option.apply(null),
                false,
                Option.apply(null),
                Option.apply(null),
                Option.apply(null),
                DefaultConnectionFactory.DEFAULT_HASH,
                ConnectionFactoryBuilder.Locator.ARRAY_MOD), ExecutionContext.global());

        codec = new MyCodec();
    }

    //    public static <T> void set(String key, T value){
//        memcached.awaitSet(key, value, Duration.Inf(), codec);
//    }
    public static void get(String key) {
        Option<String> stringOption = memcached.awaitGet(key, codec);
    }

    static class MyCodec implements Codec<String> {

        @Override
        public byte[] serialize(String value) {
            return value.getBytes(UTF_8);
        }

        @Override
        public String deserialize(byte[] data) {
            return new String(data, UTF_8);
        }
    }

}
