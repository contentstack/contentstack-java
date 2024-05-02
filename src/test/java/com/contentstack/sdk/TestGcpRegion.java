package com.contentstack.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class TestGcpRegion {
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
}