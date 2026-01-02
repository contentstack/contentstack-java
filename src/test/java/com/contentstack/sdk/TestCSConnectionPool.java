package com.contentstack.sdk;

import okhttp3.ConnectionPool;
import org.junit.jupiter.api.Test;
import java.util.concurrent.TimeUnit;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for CSConnectionPool class.
 * Tests connection pool creation with various configurations.
 */
public class TestCSConnectionPool {

    @Test
    void testCreateDefaultConnectionPool() {
        CSConnectionPool csConnectionPool = new CSConnectionPool();
        ConnectionPool pool = csConnectionPool.create();
        
        assertNotNull(pool);
    }

    @Test
    void testCreateConnectionPoolWithParameters() {
        CSConnectionPool csConnectionPool = new CSConnectionPool();
        int maxIdleConnections = 5;
        long keepAliveDuration = 300;
        TimeUnit timeUnit = TimeUnit.SECONDS;
        
        ConnectionPool pool = csConnectionPool.create(maxIdleConnections, keepAliveDuration, timeUnit);
        
        assertNotNull(pool);
    }

    @Test
    void testCreateConnectionPoolWithMinimalParameters() {
        CSConnectionPool csConnectionPool = new CSConnectionPool();
        ConnectionPool pool = csConnectionPool.create(1, 1, TimeUnit.MILLISECONDS);
        
        assertNotNull(pool);
    }

    @Test
    void testCreateConnectionPoolWithLargeValues() {
        CSConnectionPool csConnectionPool = new CSConnectionPool();
        ConnectionPool pool = csConnectionPool.create(100, 3600, TimeUnit.SECONDS);
        
        assertNotNull(pool);
    }

    @Test
    void testCreateConnectionPoolWithDifferentTimeUnits() {
        CSConnectionPool csConnectionPool = new CSConnectionPool();
        
        ConnectionPool poolSeconds = csConnectionPool.create(5, 60, TimeUnit.SECONDS);
        assertNotNull(poolSeconds);
        
        ConnectionPool poolMinutes = csConnectionPool.create(5, 5, TimeUnit.MINUTES);
        assertNotNull(poolMinutes);
        
        ConnectionPool poolHours = csConnectionPool.create(5, 1, TimeUnit.HOURS);
        assertNotNull(poolHours);
    }

    @Test
    void testMultipleConnectionPoolCreation() {
        CSConnectionPool csConnectionPool = new CSConnectionPool();
        
        ConnectionPool pool1 = csConnectionPool.create();
        ConnectionPool pool2 = csConnectionPool.create();
        ConnectionPool pool3 = csConnectionPool.create(10, 300, TimeUnit.SECONDS);
        
        assertNotNull(pool1);
        assertNotNull(pool2);
        assertNotNull(pool3);
        assertNotSame(pool1, pool2);
        assertNotSame(pool2, pool3);
    }

    @Test
    void testCSConnectionPoolInstantiation() {
        CSConnectionPool csConnectionPool1 = new CSConnectionPool();
        CSConnectionPool csConnectionPool2 = new CSConnectionPool();
        
        assertNotNull(csConnectionPool1);
        assertNotNull(csConnectionPool2);
        assertNotSame(csConnectionPool1, csConnectionPool2);
    }
}

