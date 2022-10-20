package com.contentstack.sdk;

import okhttp3.ConnectionPool;

import java.util.concurrent.TimeUnit;

public class CSConnectionPool {

    ConnectionPool create() {
        return new ConnectionPool();
    }

    ConnectionPool create(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit) {
        return new ConnectionPool(maxIdleConnections, keepAliveDuration, timeUnit);
    }

}
