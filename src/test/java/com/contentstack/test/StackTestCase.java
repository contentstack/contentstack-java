package com.contentstack.test;

import com.contentstack.sdk.Contentstack;
import com.contentstack.sdk.Stack;
import io.github.cdimascio.dotenv.Dotenv;
import org.junit.*;

public class StackTestCase {

    Stack stack;

    @BeforeClass
    public void testStackTests() throws Exception{
        Dotenv dotenv = Dotenv.load();
        String APIKey = dotenv.get("api_key");
        String deliveryToken = dotenv.get("delivery_token");
        String environment = dotenv.get("environment");
        stack = Contentstack.stack(APIKey, deliveryToken, environment);
        String apiKey = stack.getApplicationKey();
        Assert.assertEquals("478347834", apiKey);
    }

    @AfterClass
    public void finishStackTests(){

    }

    @Before
    public void runBeforeEveryTest(){

    }

    @After
    public void runAfterEveryTest(){

    }

    @Test
    public void testAPIKey() throws Exception{
        String apiKey = stack.getApplicationKey();
        Assert.assertEquals("478347834", apiKey);
    }

}
