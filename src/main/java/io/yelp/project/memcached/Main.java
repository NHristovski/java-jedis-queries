package io.yelp.project.memcached;

import net.spy.memcached.ConnectionFactoryBuilder;
import net.spy.memcached.DefaultConnectionFactory;
import scala.Option;
import scala.concurrent.ExecutionContext;
import scala.concurrent.Future;
import scala.concurrent.duration.Duration;
import scala.concurrent.duration.FiniteDuration;
import shade.memcached.BaseCodecs;
import shade.memcached.Codec;
import shade.memcached.Configuration;
import shade.memcached.FailureMode;
import shade.memcached.Memcached;
import shade.memcached.MemcachedCodecs;
import shade.memcached.Protocol;

import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeUnit;

import static java.nio.charset.StandardCharsets.*;

public class Main {
    public static void main(String[] args) {
        Memcached memcached = Memcached.apply(new Configuration("15.161.159.56:11211 15.161.189.166:11211 15.161.153.91:11211",
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

//        memcached.set("testingNikola", "test123", Duration.Inf(), new MyCodec());

//        Option<String> testingNikola = memcached.awaitGet("testingNikola", new MyCodec());
//        System.out.println(testingNikola.get());

    }
}
