package com.contentstack.sdk;

import okhttp3.ConnectionPool;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Config testcase.
 */
public class TestConfig {

    private static final Logger logger = Logger.getLogger(TestConfig.class.getName());
    private static Config config;


    @BeforeAll
    public static void setUp() {
        logger.setLevel(Level.FINE);
        config = new Config();
    }


    @Test
    void testNullProxy() {
        Assertions.assertNotNull(config.getProxy());
    }

    @Test
    void testsSetProxy() {
        java.net.Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sl.shaileshmishra.io", 80));
        config.setProxy(proxy);
        Proxy newProxy = config.getProxy();
        Assertions.assertNotNull(newProxy.address().toString());
    }

    @Test
    void testsConnectionPool() {
        ConnectionPool pool = config.connectionPool;
        pool.connectionCount();
        pool.idleConnectionCount();
        Assertions.assertNotNull(pool);
    }

    @Test
    void testsTags() {
        String[] tags = {"Java", "Programming", "Code"};
        String joinedTags = String.join(", ", tags);
        Assertions.assertNotNull(joinedTags);
    }


}
