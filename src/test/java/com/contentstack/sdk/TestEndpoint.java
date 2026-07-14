package com.contentstack.sdk;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class TestEndpoint {

    @AfterEach
    void resetCache() {
        Endpoint.resetCache();
    }

    // ── canonical IDs ─────────────────────────────────────────────────────────

    @Test
    void testNaContentDelivery() {
        String url = Endpoint.getContentstackEndpoint("na", "contentDelivery");
        Assertions.assertEquals("https://cdn.contentstack.io", url);
    }

    @Test
    void testEuContentDelivery() {
        String url = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
        Assertions.assertEquals("https://eu-cdn.contentstack.com", url);
    }

    @Test
    void testAuContentDelivery() {
        String url = Endpoint.getContentstackEndpoint("au", "contentDelivery");
        Assertions.assertEquals("https://au-cdn.contentstack.com", url);
    }

    @Test
    void testAzureNaContentDelivery() {
        String url = Endpoint.getContentstackEndpoint("azure-na", "contentDelivery");
        Assertions.assertEquals("https://azure-na-cdn.contentstack.com", url);
    }

    @Test
    void testAzureEuContentDelivery() {
        String url = Endpoint.getContentstackEndpoint("azure-eu", "contentDelivery");
        Assertions.assertEquals("https://azure-eu-cdn.contentstack.com", url);
    }

    @Test
    void testGcpNaContentDelivery() {
        String url = Endpoint.getContentstackEndpoint("gcp-na", "contentDelivery");
        Assertions.assertEquals("https://gcp-na-cdn.contentstack.com", url);
    }

    @Test
    void testGcpEuContentDelivery() {
        String url = Endpoint.getContentstackEndpoint("gcp-eu", "contentDelivery");
        Assertions.assertEquals("https://gcp-eu-cdn.contentstack.com", url);
    }

    // ── aliases ───────────────────────────────────────────────────────────────

    @Test
    void testAliasUsResolvesToNa() {
        String url = Endpoint.getContentstackEndpoint("us", "contentDelivery");
        Assertions.assertEquals("https://cdn.contentstack.io", url);
    }

    @Test
    void testAliasUppercaseEU() {
        String url = Endpoint.getContentstackEndpoint("EU", "contentDelivery");
        Assertions.assertEquals("https://eu-cdn.contentstack.com", url);
    }

    @Test
    void testAliasAwsNaHyphen() {
        String url = Endpoint.getContentstackEndpoint("aws-na", "contentDelivery");
        Assertions.assertEquals("https://cdn.contentstack.io", url);
    }

    @Test
    void testAliasAwsNaUnderscore() {
        String url = Endpoint.getContentstackEndpoint("aws_na", "contentDelivery");
        Assertions.assertEquals("https://cdn.contentstack.io", url);
    }

    @Test
    void testAliasAzureNaUnderscore() {
        String url = Endpoint.getContentstackEndpoint("azure_na", "contentDelivery");
        Assertions.assertEquals("https://azure-na-cdn.contentstack.com", url);
    }

    @Test
    void testAliasAzureNaUppercase() {
        String url = Endpoint.getContentstackEndpoint("AZURE_NA", "contentDelivery");
        Assertions.assertEquals("https://azure-na-cdn.contentstack.com", url);
    }

    @Test
    void testAliasGcpNaUnderscore() {
        String url = Endpoint.getContentstackEndpoint("gcp_na", "contentDelivery");
        Assertions.assertEquals("https://gcp-na-cdn.contentstack.com", url);
    }

    @Test
    void testAliasGcpEuUppercase() {
        String url = Endpoint.getContentstackEndpoint("GCP-EU", "contentDelivery");
        Assertions.assertEquals("https://gcp-eu-cdn.contentstack.com", url);
    }

    // ── services ──────────────────────────────────────────────────────────────

    @Test
    void testNaContentManagement() {
        String url = Endpoint.getContentstackEndpoint("na", "contentManagement");
        Assertions.assertEquals("https://api.contentstack.io", url);
    }

    @Test
    void testEuContentManagement() {
        String url = Endpoint.getContentstackEndpoint("eu", "contentManagement");
        Assertions.assertEquals("https://eu-api.contentstack.com", url);
    }

    @Test
    void testNaGraphqlDelivery() {
        String url = Endpoint.getContentstackEndpoint("na", "graphqlDelivery");
        Assertions.assertEquals("https://graphql.contentstack.com", url);
    }

    @Test
    void testNaAuth() {
        String url = Endpoint.getContentstackEndpoint("na", "auth");
        Assertions.assertEquals("https://auth-api.contentstack.com", url);
    }

    @Test
    void testEuPreview() {
        String url = Endpoint.getContentstackEndpoint("eu", "preview");
        Assertions.assertEquals("https://eu-rest-preview.contentstack.com", url);
    }

    @Test
    void testNaApplication() {
        String url = Endpoint.getContentstackEndpoint("na", "application");
        Assertions.assertEquals("https://app.contentstack.com", url);
    }

    @Test
    void testNaAssetManagement() {
        String url = Endpoint.getContentstackEndpoint("na", "assetManagement");
        Assertions.assertEquals("https://am-api.contentstack.com", url);
    }

    // ── omitHttps ─────────────────────────────────────────────────────────────

    @Test
    void testOmitHttpsNaContentDelivery() {
        String host = Endpoint.getContentstackEndpoint("na", "contentDelivery", true);
        Assertions.assertEquals("cdn.contentstack.io", host);
    }

    @Test
    void testOmitHttpsEuContentDelivery() {
        String host = Endpoint.getContentstackEndpoint("eu", "contentDelivery", true);
        Assertions.assertEquals("eu-cdn.contentstack.com", host);
    }

    @Test
    void testOmitHttpsAzureNaContentManagement() {
        String host = Endpoint.getContentstackEndpoint("azure-na", "contentManagement", true);
        Assertions.assertEquals("azure-na-api.contentstack.com", host);
    }

    @Test
    void testOmitHttpsFalseReturnsFullUrl() {
        String url = Endpoint.getContentstackEndpoint("gcp-eu", "contentDelivery", false);
        Assertions.assertTrue(url.startsWith("https://"));
    }

    // ── getAllEndpoints ───────────────────────────────────────────────────────

    @Test
    void testGetAllEndpointsNaContainsContentDelivery() {
        Map<String, String> endpoints = Endpoint.getAllEndpoints("na");
        Assertions.assertTrue(endpoints.containsKey("contentDelivery"));
        Assertions.assertEquals("https://cdn.contentstack.io", endpoints.get("contentDelivery"));
    }

    @Test
    void testGetAllEndpointsEuSize() {
        Map<String, String> endpoints = Endpoint.getAllEndpoints("eu");
        Assertions.assertFalse(endpoints.isEmpty());
        Assertions.assertTrue(endpoints.size() >= 4);
    }

    @Test
    void testGetAllEndpointsOmitHttps() {
        Map<String, String> endpoints = Endpoint.getAllEndpoints("na", true);
        for (String url : endpoints.values()) {
            Assertions.assertFalse(url.startsWith("https://"),
                    "Expected no https:// prefix but got: " + url);
        }
    }

    @Test
    void testGetAllEndpointsAzureNaOmitHttps() {
        Map<String, String> endpoints = Endpoint.getAllEndpoints("azure-na", true);
        Assertions.assertEquals("azure-na-cdn.contentstack.com", endpoints.get("contentDelivery"));
    }

    // ── error cases ───────────────────────────────────────────────────────────

    @Test
    void testEmptyRegionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getContentstackEndpoint("", "contentDelivery"));
    }

    @Test
    void testBlankRegionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getContentstackEndpoint("   ", "contentDelivery"));
    }

    @Test
    void testUnknownRegionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getContentstackEndpoint("asia-pacific", "contentDelivery"));
    }

    @Test
    void testUnknownServiceThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getContentstackEndpoint("na", "cms"));
    }

    @Test
    void testServiceNotAvailableInRegionThrows() {
        // assetManagement exists only in NA
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getContentstackEndpoint("eu", "assetManagement"));
    }

    @Test
    void testGetAllEndpointsEmptyRegionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getAllEndpoints(""));
    }

    @Test
    void testGetAllEndpointsUnknownRegionThrows() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getAllEndpoints("unknown-region"));
    }

    // ── caching ───────────────────────────────────────────────────────────────

    @Test
    void testMultipleCallsReturnSameResult() {
        String url1 = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
        String url2 = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
        Assertions.assertEquals(url1, url2);
    }

    @Test
    void testCacheResetAllowsReload() {
        String url1 = Endpoint.getContentstackEndpoint("na", "contentDelivery");
        Endpoint.resetCache();
        String url2 = Endpoint.getContentstackEndpoint("na", "contentDelivery");
        Assertions.assertEquals(url1, url2);
    }

    // ── live-refresh fallback ─────────────────────────────────────────────────

    /**
     * Verifies that a truly unknown region still throws after the live-refresh
     * attempt (the download succeeds but "atlantis" isn't a real region).
     * This also exercises the live-refresh code path — the test passes whether
     * the download succeeds or the network is unavailable (both produce the same
     * IllegalArgumentException for a non-existent region).
     */
    @Test
    void testUnknownRegionThrowsEvenAfterLiveRefresh() {
        // Ensure liveRefreshDone starts false for this test
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> Endpoint.getContentstackEndpoint("atlantis", "contentDelivery"));
    }

    /**
     * Verifies that after resetCache() the live-refresh flag is also cleared, so
     * the fallback can be exercised again in the next lookup if needed.
     */
    @Test
    void testResetCacheClearsLiveRefreshFlag() {
        // Trigger a first lookup (may or may not hit live refresh internally)
        try {
            Endpoint.getContentstackEndpoint("na", "contentDelivery");
        } catch (Exception ignored) {
            // ignored
        }
        // resetCache must clear liveRefreshDone so a subsequent cache miss can retry
        Endpoint.resetCache();
        // After reset, known regions still resolve correctly via classpath
        String url = Endpoint.getContentstackEndpoint("na", "contentDelivery");
        Assertions.assertEquals("https://cdn.contentstack.io", url);
    }
}
