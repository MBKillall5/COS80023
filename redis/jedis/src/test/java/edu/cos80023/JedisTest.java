package edu.cos80023;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import redis.clients.jedis.HostAndPort;
import redis.clients.jedis.JedisCluster;

public class JedisTest {

    private static JedisCluster jedis;
    private static final int port=7000;
    private final static Logger logger = (Logger) LogManager.getLogger(JedisTest.class);

    @BeforeClass
    public static void setUp() throws IOException {
        
        Set<HostAndPort> jedisClusterNodes = new HashSet<HostAndPort>();
        jedisClusterNodes.add(new HostAndPort("10.0.0.21", port));
        jedisClusterNodes.add(new HostAndPort("10.0.0.22", port));
        jedisClusterNodes.add(new HostAndPort("10.0.0.23", port));
        jedis = new JedisCluster(jedisClusterNodes);        
        
    }

    @AfterClass
    public static void destroy() {
        
    }

    @After
    public void flush() {

    }

    @Test
    public void givenAString_thenSaveItAsRedisStrings() {
        String key = "testkey";
        String value = "testvalue";

        jedis.set(key, value);
        String value2 = jedis.get(key);
        logger.info("key was correctly set and read");
        Assert.assertEquals(value, value2);
    }

}
