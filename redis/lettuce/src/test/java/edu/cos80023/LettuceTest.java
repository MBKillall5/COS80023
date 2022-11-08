package edu.cos80023;

import io.lettuce.core.LettuceFutures;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisFuture;
import io.lettuce.core.TransactionResult;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.async.RedisAsyncCommands;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.cluster.RedisClusterClient;
import io.lettuce.core.cluster.api.StatefulRedisClusterConnection;
import io.lettuce.core.cluster.api.async.RedisAdvancedClusterAsyncCommands;
import io.lettuce.core.cluster.api.sync.RedisAdvancedClusterCommands;
import io.lettuce.core.pubsub.RedisPubSubListener;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertTrue;

public class LettuceTest {

    private static Logger log = LoggerFactory.getLogger(LettuceTest.class);

    private static StatefulRedisClusterConnection<String, String> redisConnection;

    private static RedisClusterClient redisClient;

    @BeforeClass
    public static void setUp() {
        // Docker defaults to mapping redis port to 32768
        redisClient = RedisClusterClient.create("redis://10.0.0.21:7000,10.0.0.22:7000,10.0.0.23:7000");
        redisConnection = redisClient.connect();
    }

    @AfterClass
    public static void destroy() {
        redisConnection.close();
    }

    @Test
    public void givenAString_thenSaveItAsRedisStringsSync() {

        RedisAdvancedClusterCommands<String, String> syncCommands = redisConnection.sync();

        String key = "key";
        String value = "value";

        syncCommands.set(key, value);
        String response = syncCommands.get(key);

        Assert.assertEquals(value, response);
    }

    @Test
    public void givenValues_thenSaveAsRedisHashSync() {

        RedisAdvancedClusterCommands<String, String> syncCommands = redisConnection.sync();

        String recordName = "record1";
        String name = "FirstName";
        String value = "John";
        String surname = "LastName";
        String value1 = "Smith";

        syncCommands.hset(recordName, name, value);
        syncCommands.hset(recordName, surname, value1);
        Map<String, String> record = syncCommands.hgetall(recordName);

        Assert.assertEquals(record.get(name), value);
        Assert.assertEquals(record.get(surname), value1);
    }

    @Test
    public void givenAString_thenSaveItAsRedisStringsAsync() throws Exception {

        RedisAdvancedClusterAsyncCommands<String, String> asyncCommands = redisConnection.async();

        String key = "key";
        String value = "value";

        asyncCommands.set(key, value);
        RedisFuture<String> redisFuture = asyncCommands.get(key);

        String response = redisFuture.get();

        Assert.assertEquals(value, response);
    }

    @Test
    public void givenValues_thenSaveAsRedisHashAsync() throws Exception {

        RedisAdvancedClusterAsyncCommands<String, String> asyncCommands = redisConnection.async();

        String recordName = "record1";
        String name = "FirstName";
        String value = "John";
        String surname = "LastName";
        String value1 = "Smith";

        asyncCommands.hset(recordName, name, value);
        asyncCommands.hset(recordName, surname, value1);
        RedisFuture<Map<String, String>> redisFuture = asyncCommands.hgetall(recordName);

        Map<String, String> record = redisFuture.get();

        Assert.assertEquals(record.get(name), value);
        Assert.assertEquals(record.get(surname), value1);
    }


}
