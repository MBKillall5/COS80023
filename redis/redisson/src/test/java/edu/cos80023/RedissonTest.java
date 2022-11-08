package edu.cos80023;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import org.redisson.Redisson;
import org.redisson.RedissonMultiLock;
import org.redisson.api.*;
import org.redisson.client.RedisClient;
import org.redisson.client.RedisClientConfig;
import org.redisson.client.RedisConnection;
import org.redisson.client.codec.StringCodec;
import org.redisson.client.protocol.RedisCommands;
import org.redisson.config.Config;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class RedissonTest {
    private static RedissonClient client;

    @BeforeClass
    public static void setUp() throws IOException {
      Config config = new Config();
      config.useClusterServers().addNodeAddress("redis://10.0.0.21:7000");
      config.useClusterServers().addNodeAddress("redis://10.0.0.22:7000");
      config.useClusterServers().addNodeAddress("redis://10.0.0.23:7000");

      client = Redisson.create(config);
    }

    @AfterClass
    public static void destroy() {
        if (client != null) {
            client.shutdown();
        }
    }

    @Test
    public void givenMultipleKeysInRedis_thenGetAllKeys() {
        client.getBucket("key1").set("key1");
        client.getBucket("key2").set("key2");
        client.getBucket("key3").set("key3");

        RKeys keys = client.getKeys();

        assertTrue(keys.count() >= 3);
    }

    @Test
    public void givenAnObject_thenSaveToRedis() {
        RBucket<SomeClass> bucket = client.getBucket("someclass");
        SomeClass ledger = new SomeClass();
        ledger.setName("someclass1");
        bucket.set(ledger);

        SomeClass returnedLedger = bucket.get();

        assertTrue(
          returnedLedger != null
            && returnedLedger.getName().equals("someclass1"));
    }

   
}
