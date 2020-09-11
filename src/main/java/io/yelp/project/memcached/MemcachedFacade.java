package io.yelp.project.memcached;

import io.yelp.project.ApplicationProperties;
import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.DefaultConnectionFactory;
import scala.Function0;
import scala.Option;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import scala.reflect.ClassTag;
import shade.memcached.Codec;
import shade.memcached.Configuration;
import shade.memcached.FailureMode;
import shade.memcached.GenericCodec;
import shade.memcached.Memcached;
import shade.memcached.Protocol;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.UTF_8;

public class MemcachedFacade {

    private static Memcached memcached;
    private static Codec<HashMap<String, String>> hashMapCodec;

    public static void init(ApplicationProperties applicationProperties) {
        memcached = Memcached.apply(new Configuration(applicationProperties.getMemcachedHosts(),
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

        ClassTag<HashMap<String, String>> objectClassTag = ClassTag.apply(HashMap.class);
        hashMapCodec = new GenericCodec() {
        }.AnyRefBinaryCodec(objectClassTag);
    }

    //    public static <T> void set(String key, T value){
//        memcached.awaitSet(key, value, Duration.Inf(), codec);
//    }
    public static String get(String key) {
        Option<HashMap<String, String>> value = memcached.awaitGet(key, hashMapCodec);
        return value.toString();
    }

    public static void set(String key, HashMap<String, String> value) {
        FiniteDuration duration = Duration.apply(15, TimeUnit.SECONDS);
        memcached.awaitSet(key, value, duration, hashMapCodec);
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
