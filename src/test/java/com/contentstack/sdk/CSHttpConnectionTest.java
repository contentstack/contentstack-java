package com.contentstack.sdk;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

class CSHttpConnectionTest {

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
  };

  @Test
  void setError() {
    MockIRequestModelHTTP csConnectionRequest = new MockIRequestModelHTTP();

    CSHttpConnection connection = new CSHttpConnection("https://www.example.com", csConnectionRequest);
    connection.setError("Something bad");

    assertEquals("Something bad", csConnectionRequest.error.getString("error_message"));
    assertEquals(0, csConnectionRequest.statusCode);
  }
}