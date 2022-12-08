package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import okhttp3.Request;
import org.junit.jupiter.api.*;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.logging.Logger;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class TestContentstackPlugin {

    protected String API_KEY, DELIVERY_TOKEN, ENV;
    private final Logger logger = Logger.getLogger(TestContentstackPlugin.class.getName());

    @BeforeAll
    public void initBeforeTests() {
        Dotenv dotenv = Dotenv.load();
        API_KEY = dotenv.get("API_KEY");
        DELIVERY_TOKEN = dotenv.get("DELIVERY_TOKEN");
        ENV = dotenv.get("ENVIRONMENT");
    }

    class Plugin1 implements ContentstackPlugin {


        @Override
        public void onRequest(Stack stack, Request request) {

        }

        @Override
        public Response onResponse(Stack stack, Request request, retrofit2.Response response) {
            return null;
        }
    }


    class Plugin2 implements ContentstackPlugin {


        @Override
        public void onRequest(Stack stack, Request request) {

        }

        @Override
        public Response onResponse(Stack stack, Request request, Response response) {
            return response;
        }
    }


    @Test
    @Order(1)
    void testContentstackPlugin() {
        try {

            ArrayList<ContentstackPlugin> plugins = new ArrayList<>();
            Plugin1 plugin1 = new Plugin1();
            Plugin2 plugin2 = new Plugin2();

            plugins.add(plugin1);
            plugins.add(plugin2);


            // Create a config instance:
            Config config = new Config();
            config.setPlugins(plugins);

            Stack stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV, config);
            ContentType contentType = stack.contentType("fakeCT");
            Entry entry = contentType.entry();

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

}
