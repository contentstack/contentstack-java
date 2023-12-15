package com.contentstack.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class TestContentstack {

    private String API_KEY, DELIVERY_TOKEN, ENV;
    private final Logger logger = Logger.getLogger(TestContentstack.class.getName());

    @BeforeAll
    public void initBeforeTests() {
        API_KEY = Credentials.API_KEY;
        DELIVERY_TOKEN = Credentials.DELIVERY_TOKEN;
        ENV = Credentials.ENVIRONMENT;
    }

    @Test
    void initStackPrivateModifier() {
        try {
            new Contentstack();
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assertions.assertEquals("Can Not Access Private Modifier", e.getLocalizedMessage());
        }
    }

    @Test
    void initStackWithNullAPIKey() {
        try {
            Contentstack.stack(null, DELIVERY_TOKEN, ENV);
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assertions.assertEquals("API Key can not be null", e.getLocalizedMessage(), "Set APIKey Null");
        }
    }

    @Test
    void initStackWithNullDeliveryToken() {
        try {
            Contentstack.stack(API_KEY, null, ENV);
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assertions.assertEquals("Delivery Token can not be null", e.getLocalizedMessage(),
                    "Set deliveryToken Null");
        }
    }

    @Test
    void initStackWithNullEnvironment() {
        try {
            Contentstack.stack(API_KEY, DELIVERY_TOKEN, null);
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assertions.assertEquals("Environment can not be null", e.getLocalizedMessage(), "Set Environment Null");
        }
    }

    @Test
    void initStackWithEmptyAPIKey() {
        try {
            Contentstack.stack("", DELIVERY_TOKEN, ENV);
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assertions.assertEquals("API Key can not be empty", e.getLocalizedMessage(), "Set APIKey Null");
        }
    }

    @Test
    void initStackWithEmptyDeliveryToken() {
        try {
            Contentstack.stack(API_KEY, "", ENV);
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assertions.assertEquals("Delivery Token can not be empty", e.getLocalizedMessage(),
                    "Set deliveryToken Null");
        }
    }

    @Test
    void initStackWithEmptyEnvironment() {
        try {
            Contentstack.stack(API_KEY, DELIVERY_TOKEN, "");
        } catch (Exception e) {
            logger.info(e.getLocalizedMessage());
            Assertions.assertEquals("Environment can not be empty", e.getLocalizedMessage(), "Set Environment Null");
        }
    }

    @Test
    void initStackWithAllValidCredentials() throws IllegalAccessException {
        Stack stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV);
        Assertions.assertNotNull(stack);
    }

    @Test
    void initStackWithConfigs() throws IllegalAccessException {
        Config config = new Config();
        Stack stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV, config);
        Assertions.assertEquals("cdn.contentstack.io", config.host);
        Assertions.assertNotNull(stack);
    }


    @Test
    void testConfigEarlyAccessSingleFeature() throws IllegalAccessException {
        Config config = new Config();
        String[] earlyAccess = {"Taxonomy"};
        config.setEarlyAccess(earlyAccess);
        Stack stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV, config);
        Assertions.assertEquals(earlyAccess[0], config.earlyAccess[0]);
        Assertions.assertNotNull(stack.headers.containsKey("x-header-ea"));
        Assertions.assertEquals("Taxonomy", stack.headers.get("x-header-ea"));

    }

    @Test
    void testConfigEarlyAccessMultipleFeature() throws IllegalAccessException {
        Config config = new Config();
        String[] earlyAccess = {"Taxonomy", "Teams", "Terms", "LivePreview"};
        config.setEarlyAccess(earlyAccess);
        Stack stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV, config);
        Assertions.assertEquals(4, stack.headers.keySet().size());
        Assertions.assertEquals(earlyAccess[1], config.earlyAccess[1]);
        Assertions.assertTrue(stack.headers.containsKey("x-header-ea"));
        Assertions.assertEquals("Taxonomy,Teams,Terms,LivePreview", stack.headers.get("x-header-ea"));
    }
}
