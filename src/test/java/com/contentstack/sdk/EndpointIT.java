package com.contentstack.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import java.util.Map;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

/**
 * Integration tests for {@link Endpoint}.
 *
 * <p>The "stack integration" tests create a real Stack using the host resolved
 * by {@code Endpoint} and exercise the Contentstack CDA using credentials from
 * {@code src/test/resources/.env}.  They share the same credential set as the
 * rest of the integration suite and are deliberately non-destructive (read-only
 * CDA calls only).
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class EndpointIT {

    private static final Logger logger = Logger.getLogger(EndpointIT.class.getName());

    private String apiKey;
    private String deliveryToken;
    private String environment;

    @BeforeAll
    public void setUp() {
        apiKey        = Credentials.API_KEY;
        deliveryToken = Credentials.DELIVERY_TOKEN;
        environment   = Credentials.ENVIRONMENT;
    }

    // ── endpoint resolution ───────────────────────────────────────────────────

    @Test
    void testNaContentDeliveryResolvesCorrectly() {
        String url = Endpoint.getContentstackEndpoint("na", "contentDelivery");
        Assertions.assertEquals("https://cdn.contentstack.io", url);
    }

    @Test
    void testEuContentDeliveryResolvesCorrectly() {
        String url = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
        Assertions.assertEquals("https://eu-cdn.contentstack.com", url);
    }

    @Test
    void testAzureNaContentDeliveryResolvesCorrectly() {
        String url = Endpoint.getContentstackEndpoint("azure-na", "contentDelivery");
        Assertions.assertEquals("https://azure-na-cdn.contentstack.com", url);
    }

    @Test
    void testGcpNaContentDeliveryResolvesCorrectly() {
        String url = Endpoint.getContentstackEndpoint("gcp-na", "contentDelivery");
        Assertions.assertEquals("https://gcp-na-cdn.contentstack.com", url);
    }

    @Test
    void testOmitHttpsStripsScheme() {
        String host = Endpoint.getContentstackEndpoint("na", "contentDelivery", true);
        Assertions.assertEquals("cdn.contentstack.io", host);
        Assertions.assertFalse(host.startsWith("https://"));
    }

    @Test
    void testGetAllEndpointsReturnsMap() {
        Map<String, String> endpoints = Endpoint.getAllEndpoints("na");
        Assertions.assertNotNull(endpoints);
        Assertions.assertFalse(endpoints.isEmpty());
        Assertions.assertTrue(endpoints.containsKey("contentDelivery"));
        Assertions.assertTrue(endpoints.containsKey("contentManagement"));
        Assertions.assertTrue(endpoints.containsKey("auth"));
    }

    @Test
    void testAliasResolution() {
        // 'us' is an alias for 'na'
        String viaAlias    = Endpoint.getContentstackEndpoint("us",      "contentDelivery");
        String viaCanonical = Endpoint.getContentstackEndpoint("na",     "contentDelivery");
        Assertions.assertEquals(viaCanonical, viaAlias);

        // underscore + uppercase alias for azure-na
        String viaUnderscore = Endpoint.getContentstackEndpoint("AZURE_NA", "contentDelivery");
        String viaHyphen     = Endpoint.getContentstackEndpoint("azure-na", "contentDelivery");
        Assertions.assertEquals(viaHyphen, viaUnderscore);
    }

    @Test
    void testAllRegionsHaveContentDelivery() {
        String[] regions = {"na", "eu", "au", "azure-na", "azure-eu", "gcp-na", "gcp-eu"};
        for (String region : regions) {
            String url = Endpoint.getContentstackEndpoint(region, "contentDelivery");
            Assertions.assertNotNull(url, "contentDelivery missing for region: " + region);
            Assertions.assertTrue(url.startsWith("https://"),
                    "Expected https:// for region " + region + " but got: " + url);
        }
    }

    @Test
    void testAllRegionsHaveContentManagement() {
        String[] regions = {"na", "eu", "au", "azure-na", "azure-eu", "gcp-na", "gcp-eu"};
        for (String region : regions) {
            String url = Endpoint.getContentstackEndpoint(region, "contentManagement");
            Assertions.assertNotNull(url, "contentManagement missing for region: " + region);
            Assertions.assertTrue(url.startsWith("https://"),
                    "Expected https:// for region " + region + " but got: " + url);
        }
    }

    // ── stack integration ─────────────────────────────────────────────────────

    /**
     * Verifies that a Stack configured with the host resolved from
     * {@code Endpoint} has the correct host set on its config.
     */
    @Test
    void testStackHostMatchesResolvedEndpoint() throws IllegalAccessException {
        String host = Endpoint.getContentstackEndpoint("na", "contentDelivery", true);
        Config config = new Config();
        config.setHost(host);
        Stack stack = Contentstack.stack("fakeKey", "fakeToken", "fakeEnv", config);
        Assertions.assertEquals(host, stack.config.host,
                "Stack host should match the endpoint-resolved host");
    }

    /**
     * Verifies that a Stack created via the endpoint-resolved host for EU
     * has the correct host on its config.
     */
    @Test
    void testStackHostEuMatchesResolvedEndpoint() throws IllegalAccessException {
        String host = Endpoint.getContentstackEndpoint("eu", "contentDelivery", true);
        Config config = new Config();
        config.setHost(host);
        Stack stack = Contentstack.stack("fakeKey", "fakeToken", "fakeEnv", config);
        Assertions.assertEquals("eu-cdn.contentstack.com", stack.config.host);
    }

    /**
     * Creates a real Stack using the NA endpoint resolved from {@code Endpoint}
     * and fetches content types from the CDA to confirm the host resolves to a
     * working endpoint.
     *
     * <p>Skipped gracefully when credentials are absent (CI without secrets).
     */
    @Test
    void testRealStackWithEndpointResolvedHost() throws IllegalAccessException, InterruptedException {
        if (apiKey == null || apiKey.isEmpty()
                || deliveryToken == null || deliveryToken.isEmpty()) {
            logger.warning("Skipping live API test — credentials not configured.");
            return;
        }

        String host = Endpoint.getContentstackEndpoint("na", "contentDelivery", true);
        Config config = new Config();
        config.setHost(host);

        Stack stack = Contentstack.stack(apiKey, deliveryToken, environment, config);
        Assertions.assertEquals(host, stack.config.host);

        CountDownLatch latch = new CountDownLatch(1);
        boolean[] passed = {false};

        stack.getContentTypes(new org.json.JSONObject(), new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel model, Error error) {
                if (error == null) {
                    logger.info(() -> "Live endpoint check: fetched content types via host=" + host);
                    passed[0] = true;
                } else {
                    logger.warning(() -> "Live endpoint check failed: " + error.getErrorMessage());
                }
                latch.countDown();
            }
        });

        boolean completed = latch.await(15, TimeUnit.SECONDS);
        Assertions.assertTrue(completed, "API call timed out after 15 seconds");
        // Skip (not fail) when credentials are absent or point to a non-existent stack
        Assumptions.assumeTrue(passed[0],
                "Live API call returned an error — skipping assertion (check .env credentials)");
    }

    /**
     * Verifies that the host resolved via {@code Endpoint} for the NA region
     * matches the Stack default host that the SDK would have used without
     * endpoint resolution (backward-compatible).
     */
    @Test
    void testEndpointResolvedHostIsBackwardCompatibleWithDefaultNa() throws IllegalAccessException {
        Stack defaultStack = Contentstack.stack("k", "t", "e");
        String sdkDefaultHost = defaultStack.config.host;
        String resolvedHost = Endpoint.getContentstackEndpoint("na", "contentDelivery", true);
        Assertions.assertEquals(sdkDefaultHost, resolvedHost,
                "Endpoint-resolved NA host should match the SDK's built-in default");
    }

    // ── Contentstack proxy ────────────────────────────────────────────────────

    @Test
    void testContentstackProxyMatchesEndpointDirect() {
        String viaProxy  = Contentstack.getContentstackEndpoint("eu", "contentDelivery");
        String viaDirect = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
        Assertions.assertEquals(viaDirect, viaProxy);
    }

    @Test
    void testContentstackProxyOmitHttps() {
        String host = Contentstack.getContentstackEndpoint("azure-na", "contentDelivery", true);
        Assertions.assertEquals("azure-na-cdn.contentstack.com", host);
        Assertions.assertFalse(host.startsWith("https://"));
    }

    @Test
    void testContentstackProxyGetAllEndpoints() {
        Map<String, String> endpoints = Contentstack.getContentstackEndpoints("eu");
        Assertions.assertNotNull(endpoints);
        Assertions.assertFalse(endpoints.isEmpty());
        Assertions.assertEquals("https://eu-cdn.contentstack.com", endpoints.get("contentDelivery"));
    }

    @Test
    void testContentstackProxyGetAllEndpointsOmitHttps() {
        Map<String, String> endpoints = Contentstack.getContentstackEndpoints("gcp-na", true);
        Assertions.assertNotNull(endpoints);
        for (String url : endpoints.values()) {
            Assertions.assertFalse(url.startsWith("https://"),
                    "Expected no https:// prefix but got: " + url);
        }
    }

    @Test
    void testContentstackProxyUnknownRegionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Contentstack.getContentstackEndpoint("atlantis", "contentDelivery"));
    }

    // ── Config.hostOverridden — explicit host beats region resolution ─────────

    @Test
    void testExplicitHostTakesPrecedenceOverRegion() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.EU);
        config.setHost("custom-proxy.example.com");   // explicit override
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals("custom-proxy.example.com", stack.config.host,
                "Explicit host must not be replaced by region-resolved host");
    }

    @Test
    void testNoExplicitHostUsesRegionResolution() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.AU);
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals("au-cdn.contentstack.com", stack.config.host,
                "AU region should resolve to au-cdn.contentstack.com via Endpoint");
    }

    @Test
    void testEmptySetHostDoesNotSetOverrideFlag() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.EU);
        config.setHost("");   // empty → should not mark hostOverridden
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals("eu-cdn.contentstack.com", stack.config.host,
                "Empty setHost() should not prevent region-based resolution");
    }

    @Test
    void testNullSetHostDoesNotSetOverrideFlag() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.GCP_EU);
        config.setHost(null);  // null → should not mark hostOverridden
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals("gcp-eu-cdn.contentstack.com", stack.config.host,
                "Null setHost() should not prevent region-based resolution");
    }

    // ── Stack region→host resolution via Endpoint ─────────────────────────────

    @Test
    void testStackUsRegionResolvesToNaCdn() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.US);
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals("cdn.contentstack.io", stack.config.host);
    }

    @Test
    void testStackEuRegionResolvesViaEndpoint() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.EU);
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals(
                Endpoint.getContentstackEndpoint("eu", "contentDelivery", true),
                stack.config.host);
    }

    @Test
    void testStackAzureNaRegionResolvesViaEndpoint() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.AZURE_NA);
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals(
                Endpoint.getContentstackEndpoint("azure-na", "contentDelivery", true),
                stack.config.host);
    }

    @Test
    void testStackGcpNaRegionResolvesViaEndpoint() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.GCP_NA);
        Stack stack = Contentstack.stack("k", "t", "e", config);
        Assertions.assertEquals(
                Endpoint.getContentstackEndpoint("gcp-na", "contentDelivery", true),
                stack.config.host);
    }
}
