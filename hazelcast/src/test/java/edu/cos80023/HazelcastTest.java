package edu.cos80023;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import com.hazelcast.client.HazelcastClient;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.map.IMap;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class HazelcastTest {
    private static HazelcastInstance client;
    private final static Logger logger = (Logger) LogManager.getLogger(HazelcastTest.class);

    @BeforeClass
    public static void setUp() throws IOException {
        ClientConfig config = new ClientConfig();
        //config.setClusterName("dev");
        config.getNetworkConfig().addAddress("10.0.0.21", "10.0.0.22","10.0.0.23");
        client = HazelcastClient.newHazelcastClient(config);
        client.getLifecycleService().isRunning();
    }

    @AfterClass
    public static void destroy() {
        if (client != null) {
            client.shutdown();
        }
    }

    @Test
    public void givenMultipleKeysInHazelcast_thenGetAllKeys() {
        IMap<Object,Object> map = client.getMap("some_map");
        map.put("key","value");

        assertTrue(map.get("key").equals("value"));
    }
   
}
