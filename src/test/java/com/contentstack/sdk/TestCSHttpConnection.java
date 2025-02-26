package com.contentstack.sdk;

import org.junit.jupiter.api.*;
import org.json.JSONObject;

class TestCSHttpConnection {

    static class MockIRequestModelHTTP implements IRequestModelHTTP {
        public JSONObject error;
        public int statusCode;

        @Override
        public void sendRequest() {
            // Do nothing
        }

        @Override
        public void onRequestFailed(JSONObject error, int statusCode, ResultCallBack callBackObject) {
            this.error = error;
            this.statusCode = statusCode;
        }

        @Override
        public void onRequestFinished(CSHttpConnection request) {
            // Do nothing
        }
    }

    @Test
    void testValidJsonErrorResponse() {
        MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

        CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
        connection.setError("{\"error_message\": \"Invalid API Key\", \"error_code\": \"401\"}");

        Assertions.assertEquals("Invalid API Key", csConnectionRequest.error.getString("error_message"));
        Assertions.assertEquals(401, csConnectionRequest.error.getInt("error_code"));
    }

    @Test
    void testInvalidJsonErrorResponse() {
        MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

        CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
        connection.setError("This is not a JSON");

        Assertions.assertEquals("This is not a JSON", csConnectionRequest.error.getString("error_message"));
        Assertions.assertEquals(0, csConnectionRequest.statusCode);
    }

    @Test
    void testMissingErrorMessageField() {
        MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

        CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
        connection.setError("{\"error_code\": \"500\"}");

        Assertions.assertTrue(csConnectionRequest.error.has("error_message"));
        Assertions.assertEquals("An unknown error occurred.", csConnectionRequest.error.optString("error_message", "An unknown error occurred"));
        Assertions.assertEquals(500, csConnectionRequest.error.getInt("error_code"));
    }

    @Test
    void testMissingErrorCodeField() {
        MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

        CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
        connection.setError("{\"error_message\": \"Server error\"}");

        Assertions.assertEquals("Server error", csConnectionRequest.error.getString("error_message"));
        Assertions.assertEquals(0, csConnectionRequest.statusCode); // Default value when error_code is missing
    }

    @Test
    void testCompletelyEmptyErrorResponse() {
        MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

        CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
        connection.setError("{}");

        Assertions.assertEquals("An unknown error occurred.", csConnectionRequest.error.optString("error_message", "An unknown error occurred"));
        Assertions.assertEquals(0, csConnectionRequest.statusCode);
    }

    @Test
    void testNullErrorResponse() {
        MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

        CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
        connection.setError(null);

        Assertions.assertEquals("Unexpected error: No response received from server.", csConnectionRequest.error.getString("error_message"));
        Assertions.assertEquals(0, csConnectionRequest.statusCode);
    }

    @Test
    void testErrorResponseWithAdditionalFields() {
        MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

        CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
        connection.setError("{\"error_message\": \"Bad Request\", \"error_code\": \"400\", \"errors\": \"Missing parameter\"}");

        Assertions.assertEquals("Bad Request", csConnectionRequest.error.getString("error_message"));
        Assertions.assertEquals(400, csConnectionRequest.error.getInt("error_code"));
        Assertions.assertEquals("Missing parameter", csConnectionRequest.error.getString("errors"));
    }
}
