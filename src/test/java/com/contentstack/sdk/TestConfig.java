package com.contentstack.sdk;

import com.contentstack.sdk.Config.ContentstackRegion;
import okhttp3.ConnectionPool;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.net.InetSocketAddress;
import java.net.Proxy;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for Config class.
 */
public class TestConfig {

    private static final Logger logger = Logger.getLogger(TestConfig.class.getName());
    private Config config;

    @BeforeEach
    public void setUp() {
        logger.setLevel(Level.FINE);
        config = new Config();
    }

    @Test
    void testNullProxy() {
        Assertions.assertNull(config.getProxy());
    }

    @Test
    void testsSetProxy() {
        java.net.Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sl.shaileshmishra.io", 80));
        config.setProxy(proxy);
        Proxy newProxy = config.getProxy();
        Assertions.assertNotNull(newProxy);
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

    // ========== CONNECTION POOL TESTS ==========

    @Test
    void testConnectionPoolWithCustomParameters() {
        ConnectionPool customPool = config.connectionPool(10, 300, TimeUnit.SECONDS);
        
        assertNotNull(customPool);
        assertNotNull(config.connectionPool);
        assertEquals(customPool, config.connectionPool);
    }

    @Test
    void testConnectionPoolWithMinutes() {
        ConnectionPool pool = config.connectionPool(5, 5, TimeUnit.MINUTES);
        
        assertNotNull(pool);
        assertNotNull(config.connectionPool);
    }

    @Test
    void testConnectionPoolWithHours() {
        ConnectionPool pool = config.connectionPool(20, 1, TimeUnit.HOURS);
        
        assertNotNull(pool);
    }

    @Test
    void testConnectionPoolWithMilliseconds() {
        ConnectionPool pool = config.connectionPool(3, 30000, TimeUnit.MILLISECONDS);
        
        assertNotNull(pool);
    }

    @Test
    void testConnectionPoolMultipleTimes() {
        ConnectionPool pool1 = config.connectionPool(5, 5, TimeUnit.MINUTES);
        ConnectionPool pool2 = config.connectionPool(10, 10, TimeUnit.MINUTES);
        
        assertNotNull(pool1);
        assertNotNull(pool2);
        assertNotEquals(pool1, pool2);
        assertEquals(pool2, config.connectionPool);
    }

    // ========== REGION TESTS ==========

    @Test
    void testGetDefaultRegion() {
        ContentstackRegion region = config.getRegion();
        
        assertNotNull(region);
        assertEquals(ContentstackRegion.US, region);
    }

    @Test
    void testSetRegion() {
        ContentstackRegion newRegion = config.setRegion(ContentstackRegion.EU);
        
        assertNotNull(newRegion);
        assertEquals(ContentstackRegion.EU, newRegion);
        assertEquals(ContentstackRegion.EU, config.getRegion());
    }

    @Test
    void testSetRegionAzureNA() {
        ContentstackRegion region = config.setRegion(ContentstackRegion.AZURE_NA);
        
        assertEquals(ContentstackRegion.AZURE_NA, region);
        assertEquals(ContentstackRegion.AZURE_NA, config.getRegion());
    }

    @Test
    void testSetRegionAzureEU() {
        ContentstackRegion region = config.setRegion(ContentstackRegion.AZURE_EU);
        
        assertEquals(ContentstackRegion.AZURE_EU, region);
    }

    @Test
    void testSetRegionGcpNA() {
        config.setRegion(ContentstackRegion.GCP_NA);
        
        assertEquals(ContentstackRegion.GCP_NA, config.getRegion());
    }

    @Test
    void testSetRegionGcpEU() {
        config.setRegion(ContentstackRegion.GCP_EU);
        
        assertEquals(ContentstackRegion.GCP_EU, config.getRegion());
    }

    @Test
    void testSetRegionAU() {
        config.setRegion(ContentstackRegion.AU);
        
        assertEquals(ContentstackRegion.AU, config.getRegion());
    }

    @Test
    void testSetRegionMultipleTimes() {
        config.setRegion(ContentstackRegion.EU);
        assertEquals(ContentstackRegion.EU, config.getRegion());
        
        config.setRegion(ContentstackRegion.AZURE_NA);
        assertEquals(ContentstackRegion.AZURE_NA, config.getRegion());
        
        config.setRegion(ContentstackRegion.GCP_EU);
        assertEquals(ContentstackRegion.GCP_EU, config.getRegion());
    }

    // ========== LIVE PREVIEW TESTS ==========

    @Test
    void testEnableLivePreviewTrue() {
        Config result = config.enableLivePreview(true);
        
        assertNotNull(result);
        assertEquals(config, result); // Should return this for chaining
        assertTrue(config.enableLivePreview);
    }

    @Test
    void testEnableLivePreviewFalse() {
        Config result = config.enableLivePreview(false);
        
        assertNotNull(result);
        assertFalse(config.enableLivePreview);
    }

    @Test
    void testEnableLivePreviewChaining() {
        Config result = config.enableLivePreview(true)
                              .setLivePreviewHost("preview.contentstack.io");
        
        assertNotNull(result);
        assertEquals(config, result);
        assertTrue(config.enableLivePreview);
    }

    @Test
    void testSetLivePreviewHost() {
        Config result = config.setLivePreviewHost("custom-preview.example.com");
        
        assertNotNull(result);
        assertEquals(config, result);
        assertEquals("custom-preview.example.com", config.livePreviewHost);
    }

    @Test
    void testSetLivePreviewHostWithDefaultValue() {
        Config result = config.setLivePreviewHost("preview.contentstack.io");
        
        assertNotNull(result);
        assertEquals("preview.contentstack.io", config.livePreviewHost);
    }

    @Test
    void testSetLivePreviewHostMultipleTimes() {
        config.setLivePreviewHost("host1.example.com");
        assertEquals("host1.example.com", config.livePreviewHost);
        
        config.setLivePreviewHost("host2.example.com");
        assertEquals("host2.example.com", config.livePreviewHost);
    }

    @Test
    void testSetLivePreviewEntry() {
        JSONObject entry = new JSONObject();
        entry.put("uid", "entry_uid_123");
        entry.put("title", "Preview Entry");
        
        Config result = config.setLivePreviewEntry(entry);
        
        assertNotNull(result);
        assertEquals(config, result);
        assertNotNull(config.livePreviewEntry);
        assertEquals("entry_uid_123", config.livePreviewEntry.opt("uid"));
        assertEquals("Preview Entry", config.livePreviewEntry.opt("title"));
    }

    @Test
    void testSetLivePreviewEntryWithEmptyObject() {
        JSONObject emptyEntry = new JSONObject();
        
        Config result = config.setLivePreviewEntry(emptyEntry);
        
        assertNotNull(result);
        assertNotNull(config.livePreviewEntry);
        assertTrue(config.livePreviewEntry.isEmpty());
    }

    @Test
    void testSetLivePreviewEntryChaining() {
        JSONObject entry = new JSONObject();
        entry.put("content_type", "blog_post");
        
        Config result = config.enableLivePreview(true)
                              .setLivePreviewHost("preview.example.com")
                              .setLivePreviewEntry(entry);
        
        assertNotNull(result);
        assertTrue(config.enableLivePreview);
        assertEquals("preview.example.com", config.livePreviewHost);
        assertEquals("blog_post", config.livePreviewEntry.opt("content_type"));
    }

    // ========== PREVIEW TOKEN TESTS ==========

    @Test
    void testSetPreviewToken() {
        Config result = config.setPreviewToken("preview_token_12345");
        
        assertNotNull(result);
        assertEquals(config, result);
        assertEquals("preview_token_12345", config.previewToken);
    }

    @Test
    void testSetPreviewTokenChaining() {
        Config result = config.setPreviewToken("token_abc")
                              .enableLivePreview(true);
        
        assertNotNull(result);
        assertEquals("token_abc", config.previewToken);
        assertTrue(config.enableLivePreview);
    }

    @Test
    void testSetPreviewTokenMultipleTimes() {
        config.setPreviewToken("token1");
        assertEquals("token1", config.previewToken);
        
        config.setPreviewToken("token2");
        assertEquals("token2", config.previewToken);
    }

    // ========== MANAGEMENT TOKEN TESTS ==========

    @Test
    void testSetManagementToken() {
        Config result = config.setManagementToken("management_token_xyz");
        
        assertNotNull(result);
        assertEquals(config, result);
        assertEquals("management_token_xyz", config.managementToken);
    }

    @Test
    void testSetManagementTokenChaining() {
        Config result = config.setManagementToken("mgmt_token")
                              .setPreviewToken("preview_token");
        
        assertNotNull(result);
        assertEquals("mgmt_token", config.managementToken);
        assertEquals("preview_token", config.previewToken);
    }

    @Test
    void testSetManagementTokenMultipleTimes() {
        config.setManagementToken("token_a");
        assertEquals("token_a", config.managementToken);
        
        config.setManagementToken("token_b");
        assertEquals("token_b", config.managementToken);
    }

    // ========== COMPREHENSIVE CHAINING TESTS ==========

    @Test
    void testCompleteConfigurationChaining() {
        JSONObject liveEntry = new JSONObject();
        liveEntry.put("uid", "entry_123");
        
        Config result = config
                .enableLivePreview(true)
                .setLivePreviewHost("preview.contentstack.io")
                .setLivePreviewEntry(liveEntry)
                .setPreviewToken("preview_token")
                .setManagementToken("management_token");
        
        assertNotNull(result);
        assertEquals(config, result);
        assertTrue(config.enableLivePreview);
        assertEquals("preview.contentstack.io", config.livePreviewHost);
        assertNotNull(config.livePreviewEntry);
        assertEquals("preview_token", config.previewToken);
        assertEquals("management_token", config.managementToken);
    }

    @Test
    void testHostAndVersionGetters() {
        String host = config.getHost();
        String version = config.getVersion();
        
        assertNotNull(host);
        assertNotNull(version);
        assertEquals("cdn.contentstack.io", host);
        assertEquals("v3", version);
    }

    @Test
    void testSetHost() {
        config.setHost("custom.contentstack.io");
        
        assertEquals("custom.contentstack.io", config.getHost());
    }

    @Test
    void testSetHostWithEmptyString() {
        String originalHost = config.getHost();
        config.setHost("");
        
        // Empty string should not change the host
        assertEquals(originalHost, config.getHost());
    }

    @Test
    void testSetHostWithNull() {
        String originalHost = config.getHost();
        config.setHost(null);
        
        // Null should not change the host
        assertEquals(originalHost, config.getHost());
    }

    @Test
    void testBranchGetterAndSetter() {
        config.setBranch("development");
        
        assertEquals("development", config.getBranch());
    }

    @Test
    void testEarlyAccessGetterAndSetter() {
        String[] earlyAccessHeaders = {"Taxonomy", "Teams"};
        Config result = config.setEarlyAccess(earlyAccessHeaders);
        
        assertNotNull(result);
        assertArrayEquals(earlyAccessHeaders, config.getEarlyAccess());
    }
}
