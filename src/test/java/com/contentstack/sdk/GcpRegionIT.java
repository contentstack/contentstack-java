package com.contentstack.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class GcpRegionIT {
    @Test
    void testGcpRegionBehaviourGcpNA() {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.GCP_NA;
        config.setRegion(region);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("GCP_NA", config.region.name());
    }

    @Test
    void testGcpNaRegionBehaviourGcpStack() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.GCP_NA;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("GCP_NA", stack.config.region.name());
    }

    @Test
    void testGcpNARegionBehaviourGcpStackHost() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.GCP_NA;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("gcp-na-cdn.contentstack.com", stack.config.host);

    }

    @Test
    void testGcpEURegionBehaviourGcpStack() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.GCP_EU;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("GCP_EU", stack.config.region.name());
        Assertions.assertEquals("gcp-eu-cdn.contentstack.com", stack.config.host);
    }

    @Test
    void testGcpRegionWithMultipleConfigs() throws IllegalAccessException {
        // Test NA region
        Config configNA = new Config();
        configNA.setRegion(Config.ContentstackRegion.GCP_NA);
        Stack stackNA = Contentstack.stack("apiKey1", "token1", "env1", configNA);
        Assertions.assertEquals("GCP_NA", stackNA.config.region.name());
        Assertions.assertEquals("gcp-na-cdn.contentstack.com", stackNA.config.host);

        // Test EU region
        Config configEU = new Config();
        configEU.setRegion(Config.ContentstackRegion.GCP_EU);
        Stack stackEU = Contentstack.stack("apiKey2", "token2", "env2", configEU);
        Assertions.assertEquals("GCP_EU", stackEU.config.region.name());
        Assertions.assertEquals("gcp-eu-cdn.contentstack.com", stackEU.config.host);
    }

    @Test
    void testGcpRegionConfigNotNull() {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.GCP_NA;
        config.setRegion(region);
        Assertions.assertNotNull(config.region);
        Assertions.assertNotNull(config.region.name());
    }

    @Test
    void testGcpNARegionHostFormat() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.GCP_NA);
        Stack stack = Contentstack.stack("testKey", "testToken", "testEnv", config);
        String host = stack.config.host;
        Assertions.assertTrue(host.contains("gcp"));
        Assertions.assertTrue(host.contains("contentstack.com"));
        Assertions.assertTrue(host.endsWith(".com"));
    }

    @Test
    void testGcpEURegionHostFormat() throws IllegalAccessException {
        Config config = new Config();
        config.setRegion(Config.ContentstackRegion.GCP_EU);
        Stack stack = Contentstack.stack("testKey", "testToken", "testEnv", config);
        String host = stack.config.host;
        Assertions.assertTrue(host.contains("gcp"));
        Assertions.assertTrue(host.contains("eu"));
        Assertions.assertTrue(host.contains("contentstack.com"));
    }

    @Test
    void testRegionNameNotEmpty() {
        Config.ContentstackRegion gcpNA = Config.ContentstackRegion.GCP_NA;
        Config.ContentstackRegion gcpEU = Config.ContentstackRegion.GCP_EU;
        Assertions.assertFalse(gcpNA.name().isEmpty());
        Assertions.assertFalse(gcpEU.name().isEmpty());
        Assertions.assertNotEquals(gcpNA.name(), gcpEU.name());
    }
}