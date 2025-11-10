package com.contentstack.sdk;

import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for ContentTypesCallback class.
 */
public class TestContentTypesCallback {

    @Test
    void testOnRequestFinishCallsOnCompletion() {
        // Track if onCompletion was called and with what parameters
        AtomicBoolean onCompletionCalled = new AtomicBoolean(false);
        AtomicReference<ContentTypesModel> receivedModel = new AtomicReference<>();
        AtomicReference<Error> receivedError = new AtomicReference<>();

        // Create a concrete implementation
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                onCompletionCalled.set(true);
                receivedModel.set(contentTypesModel);
                receivedError.set(error);
            }
        };

        // Create a dummy ContentTypesModel
        JSONObject response = new JSONObject();
        response.put("uid", "test_content_type");
        response.put("title", "Test Content Type");
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);

        // Call onRequestFinish
        callback.onRequestFinish(model);

        // Verify onCompletion was called with the model and null error
        assertTrue(onCompletionCalled.get());
        assertNotNull(receivedModel.get());
        assertEquals(model, receivedModel.get());
        assertNull(receivedError.get());
    }

    @Test
    void testOnRequestFinishWithNullModel() {
        AtomicBoolean onCompletionCalled = new AtomicBoolean(false);
        AtomicReference<ContentTypesModel> receivedModel = new AtomicReference<>();
        AtomicReference<Error> receivedError = new AtomicReference<>();

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                onCompletionCalled.set(true);
                receivedModel.set(contentTypesModel);
                receivedError.set(error);
            }
        };

        // Call onRequestFinish with null
        callback.onRequestFinish(null);

        // Verify onCompletion was called with null model and null error
        assertTrue(onCompletionCalled.get());
        assertNull(receivedModel.get());
        assertNull(receivedError.get());
    }

    @Test
    void testOnRequestFailCallsOnCompletion() {
        AtomicBoolean onCompletionCalled = new AtomicBoolean(false);
        AtomicReference<ContentTypesModel> receivedModel = new AtomicReference<>();
        AtomicReference<Error> receivedError = new AtomicReference<>();

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                onCompletionCalled.set(true);
                receivedModel.set(contentTypesModel);
                receivedError.set(error);
            }
        };

        // Create an error
        Error error = new Error();
        error.setErrorMessage("Test error message");
        error.setErrorCode(404);

        // Call onRequestFail
        callback.onRequestFail(ResponseType.NETWORK, error);

        // Verify onCompletion was called with null model and the error
        assertTrue(onCompletionCalled.get());
        assertNull(receivedModel.get());
        assertNotNull(receivedError.get());
        assertEquals(error, receivedError.get());
        assertEquals("Test error message", receivedError.get().getErrorMessage());
        assertEquals(404, receivedError.get().getErrorCode());
    }

    @Test
    void testOnRequestFailWithNullError() {
        AtomicBoolean onCompletionCalled = new AtomicBoolean(false);
        AtomicReference<ContentTypesModel> receivedModel = new AtomicReference<>();
        AtomicReference<Error> receivedError = new AtomicReference<>();

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                onCompletionCalled.set(true);
                receivedModel.set(contentTypesModel);
                receivedError.set(error);
            }
        };

        // Call onRequestFail with null error
        callback.onRequestFail(ResponseType.UNKNOWN, null);

        // Verify onCompletion was called with null model and null error
        assertTrue(onCompletionCalled.get());
        assertNull(receivedModel.get());
        assertNull(receivedError.get());
    }

    @Test
    void testOnRequestFailWithDifferentResponseTypes() {
        // Test with NETWORK response type
        AtomicReference<ResponseType> receivedResponseType = new AtomicReference<>();
        
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                // Capture for verification
            }
        };

        Error error = new Error();
        error.setErrorMessage("Network error");

        // Test NETWORK
        assertDoesNotThrow(() -> callback.onRequestFail(ResponseType.NETWORK, error));

        // Test UNKNOWN
        assertDoesNotThrow(() -> callback.onRequestFail(ResponseType.UNKNOWN, error));
    }

    @Test
    void testMultipleOnRequestFinishCalls() {
        // Counter to track how many times onCompletion is called
        final int[] callCount = {0};

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                callCount[0]++;
            }
        };

        JSONObject response1 = new JSONObject();
        response1.put("uid", "content_type_1");
        ContentTypesModel model1 = new ContentTypesModel();
        model1.setJSON(response1);

        JSONObject response2 = new JSONObject();
        response2.put("uid", "content_type_2");
        ContentTypesModel model2 = new ContentTypesModel();
        model2.setJSON(response2);

        // Call multiple times
        callback.onRequestFinish(model1);
        callback.onRequestFinish(model2);
        callback.onRequestFinish(null);

        // Verify onCompletion was called 3 times
        assertEquals(3, callCount[0]);
    }

    @Test
    void testMultipleOnRequestFailCalls() {
        final int[] callCount = {0};

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                callCount[0]++;
            }
        };

        Error error1 = new Error();
        error1.setErrorMessage("Error 1");

        Error error2 = new Error();
        error2.setErrorMessage("Error 2");

        // Call multiple times
        callback.onRequestFail(ResponseType.NETWORK, error1);
        callback.onRequestFail(ResponseType.UNKNOWN, error2);
        callback.onRequestFail(ResponseType.NETWORK, null);

        // Verify onCompletion was called 3 times
        assertEquals(3, callCount[0]);
    }

    @Test
    void testOnCompletionWithCompleteContentTypesModel() {
        AtomicReference<ContentTypesModel> receivedModel = new AtomicReference<>();

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                receivedModel.set(contentTypesModel);
            }
        };

        // Create a complete model with multiple fields
        JSONObject response = new JSONObject();
        response.put("uid", "blog_post");
        response.put("title", "Blog Post");
        response.put("description", "Blog post content type");
        
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);

        callback.onRequestFinish(model);

        assertNotNull(receivedModel.get());
        assertEquals(model, receivedModel.get());
    }

    @Test
    void testOnCompletionWithCompleteError() {
        AtomicReference<Error> receivedError = new AtomicReference<>();

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                receivedError.set(error);
            }
        };

        // Create a complete error
        Error error = new Error();
        error.setErrorMessage("Unauthorized access");
        error.setErrorCode(401);
        error.setErrorDetail("Invalid API key");

        callback.onRequestFail(ResponseType.NETWORK, error);

        assertNotNull(receivedError.get());
        assertEquals("Unauthorized access", receivedError.get().getErrorMessage());
        assertEquals(401, receivedError.get().getErrorCode());
        assertEquals("Invalid API key", receivedError.get().getErrorDetail());
    }

    @Test
    void testCallbackImplementationRequiresOnCompletion() {
        // Verify that the abstract method must be implemented
        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                // Implementation required
                assertNotNull(this); // Just to have some assertion
            }
        };

        assertNotNull(callback);
    }

    @Test
    void testOnRequestFinishAndFailInteraction() {
        // Test that both methods can be called on the same callback instance
        final int[] successCount = {0};
        final int[] failCount = {0};

        ContentTypesCallback callback = new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                if (error == null) {
                    successCount[0]++;
                } else {
                    failCount[0]++;
                }
            }
        };

        JSONObject response = new JSONObject();
        response.put("uid", "test");
        ContentTypesModel model = new ContentTypesModel();
        model.setJSON(response);

        Error error = new Error();
        error.setErrorMessage("Test error");

        // Call both methods
        callback.onRequestFinish(model);
        callback.onRequestFail(ResponseType.NETWORK, error);
        callback.onRequestFinish(model);

        assertEquals(2, successCount[0]);
        assertEquals(1, failCount[0]);
    }
}

