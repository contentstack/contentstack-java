package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.util.logging.Logger;

class TestAzureRegion {


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
    void testAzureRegionBehaviourAzure() {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_NA;
        config.setRegion(region);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("AZURE_NA", config.region.name());
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
    void testAzureRegionBehaviourAzureStackHost() throws IllegalAccessException {
        Config config = new Config();
        Config.ContentstackRegion region = Config.ContentstackRegion.AZURE_NA;
        config.setRegion(region);
        Stack stack = Contentstack.stack("fakeApiKey", "fakeDeliveryToken", "fakeEnvironment", config);
        Assertions.assertFalse(config.region.name().isEmpty());
        Assertions.assertEquals("azure-na-cdn.contentstack.com", stack.config.host);
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
