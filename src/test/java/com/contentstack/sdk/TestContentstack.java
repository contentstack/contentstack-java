package com.contentstack.sdk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the Contentstack class.
 * Tests stack creation, validation, and error handling.
 */
public class TestContentstack {

    @Test
    void testCannotInstantiateContentstackDirectly() {
        assertThrows(IllegalAccessException.class, () -> {
            new Contentstack();
        });
    }

    @Test
    void testCreateStackWithValidCredentials() throws IllegalAccessException {
        Stack stack = Contentstack.stack("test_api_key", "test_delivery_token", "test_environment");
        
        assertNotNull(stack);
        assertNotNull(stack.headers);
        assertEquals("test_api_key", stack.headers.get("api_key"));
        assertEquals("test_delivery_token", stack.headers.get("access_token"));
        assertEquals("test_environment", stack.headers.get("environment"));
    }

    @Test
    void testCreateStackWithConfig() throws IllegalAccessException {
        Config config = new Config();
        config.setHost("custom-host.contentstack.com");
        
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment", config);
        
        assertNotNull(stack);
        assertNotNull(stack.config);
        assertEquals("custom-host.contentstack.com", stack.config.getHost());
    }

    @Test
    void testCreateStackWithBranch() throws IllegalAccessException {
        Config config = new Config();
        config.setBranch("test-branch");
        
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment", config);
        
        assertNotNull(stack);
        assertTrue(stack.headers.containsKey("branch"));
        assertEquals("test-branch", stack.headers.get("branch"));
    }

    @Test
    void testCreateStackWithEarlyAccess() throws IllegalAccessException {
        Config config = new Config();
        config.setEarlyAccess(new String[]{"feature1", "feature2"});
        
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment", config);
        
        assertNotNull(stack);
        assertTrue(stack.headers.containsKey("x-header-ea"));
        String eaHeader = (String) stack.headers.get("x-header-ea");
        assertTrue(eaHeader.contains("feature1"));
        assertTrue(eaHeader.contains("feature2"));
    }

    @Test
    void testCreateStackWithRegion() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.EU);
        
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment", config);
        
        assertNotNull(stack);
        assertEquals(Config.ContentstackRegion.EU, stack.config.region);
    }

    // ========== VALIDATION TESTS ==========

    @Test
    void testStackCreationWithNullApiKey() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Contentstack.stack(null, "delivery_token", "environment");
        });
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("API Key"));
    }

    @Test
    void testStackCreationWithNullDeliveryToken() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Contentstack.stack("api_key", null, "environment");
        });
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Delivery Token"));
    }

    @Test
    void testStackCreationWithNullEnvironment() {
        NullPointerException exception = assertThrows(NullPointerException.class, () -> {
            Contentstack.stack("api_key", "delivery_token", null);
        });
        assertNotNull(exception);
        assertTrue(exception.getMessage().contains("Environment"));
    }

    @Test
    void testStackCreationWithEmptyApiKey() {
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            Contentstack.stack("", "delivery_token", "environment");
        });
        assertEquals(ErrorMessages.MISSING_API_KEY, exception.getMessage());
    }

    @Test
    void testStackCreationWithEmptyDeliveryToken() {
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            Contentstack.stack("api_key", "", "environment");
        });
        assertEquals(ErrorMessages.MISSING_DELIVERY_TOKEN, exception.getMessage());
    }

    @Test
    void testStackCreationWithEmptyEnvironment() {
        IllegalAccessException exception = assertThrows(IllegalAccessException.class, () -> {
            Contentstack.stack("api_key", "delivery_token", "");
        });
        assertEquals(ErrorMessages.MISSING_ENVIRONMENT, exception.getMessage());
    }

    @Test
    void testStackCreationWithWhitespaceApiKey() throws IllegalAccessException {
        Stack stack = Contentstack.stack("  api_key  ", "delivery_token", "environment");
        
        assertNotNull(stack);
        assertEquals("api_key", stack.apiKey); // Should be trimmed
    }

    // ========== MULTIPLE STACK CREATION TESTS ==========

    @Test
    void testCreateMultipleStacks() throws IllegalAccessException {
        Stack stack1 = Contentstack.stack("api_key1", "token1", "env1");
        Stack stack2 = Contentstack.stack("api_key2", "token2", "env2");
        Stack stack3 = Contentstack.stack("api_key3", "token3", "env3");
        
        assertNotNull(stack1);
        assertNotNull(stack2);
        assertNotNull(stack3);
        assertNotSame(stack1, stack2);
        assertNotSame(stack2, stack3);
    }

    @Test
    void testCreateStacksWithDifferentConfigs() throws IllegalAccessException {
        Config config1 = new Config();
        config1.setRegion(Config.ContentstackRegion.US);
        
        Config config2 = new Config();
        config2.setRegion(Config.ContentstackRegion.EU);
        
        Stack stack1 = Contentstack.stack("api1", "token1", "env1", config1);
        Stack stack2 = Contentstack.stack("api2", "token2", "env2", config2);
        
        assertNotNull(stack1);
        assertNotNull(stack2);
        assertEquals(Config.ContentstackRegion.US, stack1.config.region);
        assertEquals(Config.ContentstackRegion.EU, stack2.config.region);
    }

    // ========== HEADER VALIDATION TESTS ==========

    @Test
    void testStackHeadersAreSetCorrectly() throws IllegalAccessException {
        Stack stack = Contentstack.stack("my_api_key", "my_token", "my_env");
        
        assertNotNull(stack.headers);
        assertTrue(stack.headers.size() >= 3);
        assertTrue(stack.headers.containsKey("api_key"));
        assertTrue(stack.headers.containsKey("access_token"));
        assertTrue(stack.headers.containsKey("environment"));
    }

    @Test
    void testStackWithEmptyEarlyAccess() throws IllegalAccessException {
        Config config = new Config();
        config.setEarlyAccess(new String[]{});
        
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment", config);
        
        assertNotNull(stack);
        assertFalse(stack.headers.containsKey("x-header-ea"));
    }

    @Test
    void testStackWithNullBranch() throws IllegalAccessException {
        Config config = new Config();
        config.setBranch(null);
        
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment", config);
        
        assertNotNull(stack);
        assertFalse(stack.headers.containsKey("branch"));
    }

    @Test
    void testStackWithEmptyBranch() throws IllegalAccessException {
        Config config = new Config();
        config.setBranch("");
        
        Stack stack = Contentstack.stack("api_key", "delivery_token", "environment", config);
        
        assertNotNull(stack);
        assertFalse(stack.headers.containsKey("branch"));
    }
}

