package com.contentstack.sdk;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class AzureRegionIT {

    @Test
    void testAzureRegionBehaviourUS() {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.US;
        config.setRegion(region);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("US", config.region.name());
    }

    @Test
    void testAzureRegionBehaviourEU() {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.EU;
        config.setRegion(region);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("EU", config.region.name());
    }

    @Test
    void testAzureRegionBehaviourAzureNA() {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_NA;
        config.setRegion(region);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("AZURE_NA", config.region.name());
    }

    @Test
    void testAzureRegionBehaviourAzureEU() {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_EU;
        config.setRegion(region);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("AZURE_EU", config.region.name());
    }

    @Test
    void testAzureRegionBehaviourAzureStack() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_NA;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("AZURE_NA", stack.config.region.name());
    }


    @Test
    void testAzureEURegionBehaviourAzureStack() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_EU;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("AZURE_EU", stack.config.region.name());
    }


    @Test
    void testAzureRegionBehaviourAzureStackHost() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_NA;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("azure-na-cdn.contentstack.com", stack.config.host);
    }


    @Test
    void testAzureEURegionBehaviourAzureStackHost() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_EU;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("azure-eu-cdn.contentstack.com", stack.config.host);
    }

    @Test
    void testAzureRegionBehaviourAzureStackInit() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_NA;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Query query = stack.contentType("fakeCT").query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                System.out.println("So something here...");
            }
        });
        Assertions.assertEquals("azure-na-cdn.contentstack.com", stack.config.host);
    }

    @Test
    void testAzureRegionBehaviourEUStack() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.EU;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Query query = stack.contentType("fakeCT").query();
        query.find(new QueryResultsCallBack() {
            @Override
            public void onCompletion(ResponseType responseType, QueryResult queryresult, Error error) {
                System.out.println("So something here...");
            }
        });
        Assertions.assertEquals("eu-cdn.contentstack.com", stack.config.host);
    }
}
