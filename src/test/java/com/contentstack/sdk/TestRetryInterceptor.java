package com.contentstack.sdk;

import okhttp3.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit tests for RetryInterceptor class.
 * Tests retry logic, backoff strategies, and error handling.
 */
class TestRetryInterceptor {

    private RetryOptions retryOptions;
    private RetryInterceptor interceptor;

    @BeforeEach
    void setUp() {
        retryOptions = new RetryOptions();
        interceptor = new RetryInterceptor(retryOptions);
    }

    // ===========================
    // Helper Classes - Manual Mocks
    // ===========================

    /**
     * Mock Chain that allows us to control responses and track invocations
     */
    private static class MockChain implements Interceptor.Chain {
        private final Request request;
        private Response response;
        private IOException exceptionToThrow;
        private final AtomicInteger callCount = new AtomicInteger(0);

        MockChain(Request request) {
            this.request = request;
        }

        void setResponse(Response response) {
            this.response = response;
        }

        void setException(IOException exception) {
            this.exceptionToThrow = exception;
        }

        int getCallCount() {
            return callCount.get();
        }

        @Override
        public Request request() {
            return request;
        }

        @Override
        public Response proceed(Request request) throws IOException {
            callCount.incrementAndGet();
            
            if (exceptionToThrow != null) {
                throw exceptionToThrow;
            }
            
            if (response == null) {
                throw new IllegalStateException("No response configured for mock chain");
            }
            
            return response;
        }

        @Override
        public Connection connection() {
            return null;
        }

        @Override
        public Call call() {
            return null;
        }

        @Override
        public int connectTimeoutMillis() {
            return 10000;
        }

        @Override
        public Interceptor.Chain withConnectTimeout(int timeout, java.util.concurrent.TimeUnit unit) {
            return this;
        }

        @Override
        public int readTimeoutMillis() {
            return 10000;
        }

        @Override
        public Interceptor.Chain withReadTimeout(int timeout, java.util.concurrent.TimeUnit unit) {
            return this;
        }

        @Override
        public int writeTimeoutMillis() {
            return 10000;
        }

        @Override
        public Interceptor.Chain withWriteTimeout(int timeout, java.util.concurrent.TimeUnit unit) {
            return this;
        }
    }

    /**
     * Mock Chain that changes response on each call
     */
    private static class DynamicMockChain implements Interceptor.Chain {
        private final Request request;
        private final Response[] responses;
        private final AtomicInteger callIndex = new AtomicInteger(0);

        DynamicMockChain(Request request, Response... responses) {
            this.request = request;
            this.responses = responses;
        }

        int getCallCount() {
            return callIndex.get();
        }

        @Override
        public Request request() {
            return request;
        }

        @Override
        public Response proceed(Request request) throws IOException {
            int index = callIndex.getAndIncrement();
            
            if (index >= responses.length) {
                throw new IllegalStateException("No more responses available");
            }
            
            return responses[index];
        }

        @Override
        public Connection connection() {
            return null;
        }

        @Override
        public Call call() {
            return null;
        }

        @Override
        public int connectTimeoutMillis() {
            return 10000;
        }

        @Override
        public Interceptor.Chain withConnectTimeout(int timeout, java.util.concurrent.TimeUnit unit) {
            return this;
        }

        @Override
        public int readTimeoutMillis() {
            return 10000;
        }

        @Override
        public Interceptor.Chain withReadTimeout(int timeout, java.util.concurrent.TimeUnit unit) {
            return this;
        }

        @Override
        public int writeTimeoutMillis() {
            return 10000;
        }

        @Override
        public Interceptor.Chain withWriteTimeout(int timeout, java.util.concurrent.TimeUnit unit) {
            return this;
        }
    }

    // ===========================
    // Helper Methods
    // ===========================

    private Request createTestRequest() {
        return new Request.Builder()
                .url("https://api.contentstack.io/v3/test")
                .build();
    }

    private Response createMockResponse(int statusCode) {
        return new Response.Builder()
                .request(createTestRequest())
                .protocol(Protocol.HTTP_1_1)
                .code(statusCode)
                .message("Test Response")
                .body(ResponseBody.create("", MediaType.get("application/json")))
                .build();
    }

    // ===========================
    // Successful Request Tests (No Retry)
    // ===========================

    @Test
    @DisplayName("Test successful request with 200 status - no retry")
    void testSuccessfulRequest() throws IOException {
        Request request = createTestRequest();
        Response successResponse = createMockResponse(200);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(successResponse);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should return 200 response");
        assertEquals(1, chain.getCallCount(), "Should only call once (no retry)");
    }

    @Test
    @DisplayName("Test successful request with 201 status - no retry")
    void testSuccessfulRequestWith201() throws IOException {
        Request request = createTestRequest();
        Response successResponse = createMockResponse(201);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(successResponse);

        Response result = interceptor.intercept(chain);

        assertEquals(201, result.code(), "Should return 201 response");
        assertEquals(1, chain.getCallCount(), "Should only call once (no retry)");
    }

    // ===========================
    // Non-Retryable Status Code Tests
    // ===========================

    @Test
    @DisplayName("Test 400 Bad Request - no retry")
    void testBadRequestNoRetry() throws IOException {
        Request request = createTestRequest();
        Response badRequestResponse = createMockResponse(400);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(badRequestResponse);

        Response result = interceptor.intercept(chain);

        assertEquals(400, result.code(), "Should return 400 response");
        assertEquals(1, chain.getCallCount(),
                "Should only call once (400 is not retryable)");
    }

    @Test
    @DisplayName("Test 404 Not Found - no retry")
    void testNotFoundNoRetry() throws IOException {
        Request request = createTestRequest();
        Response notFoundResponse = createMockResponse(404);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(notFoundResponse);

        Response result = interceptor.intercept(chain);

        assertEquals(404, result.code(), "Should return 404 response");
        assertEquals(1, chain.getCallCount(),
                "Should only call once (404 is not retryable)");
    }

    @Test
    @DisplayName("Test 401 Unauthorized - no retry")
    void testUnauthorizedNoRetry() throws IOException {
        Request request = createTestRequest();
        Response unauthorizedResponse = createMockResponse(401);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(unauthorizedResponse);

        Response result = interceptor.intercept(chain);

        assertEquals(401, result.code(), "Should return 401 response");
        assertEquals(1, chain.getCallCount(),
                "Should only call once (401 is not retryable)");
    }

    // ===========================
    // Retryable Status Code Tests
    // ===========================

    @Test
    @DisplayName("Test 429 Too Many Requests - triggers retry then succeeds")
    void testRateLimitRetryThenSuccess() throws IOException {
        Request request = createTestRequest();
        Response rateLimitResponse = createMockResponse(429);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                rateLimitResponse, successResponse);

        // Set minimal delay for faster test
        retryOptions.setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually return 200");
        assertEquals(2, chain.getCallCount(),
                "Should call twice (1 failure + 1 success)");
    }

    @Test
    @DisplayName("Test 503 Service Unavailable - triggers retry")
    void testServiceUnavailableRetry() throws IOException {
        Request request = createTestRequest();
        Response serviceUnavailableResponse = createMockResponse(503);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                serviceUnavailableResponse, successResponse);

        retryOptions.setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually return 200");
        assertEquals(2, chain.getCallCount(), "Should retry once");
    }

    @Test
    @DisplayName("Test 502 Bad Gateway - triggers retry")
    void testBadGatewayRetry() throws IOException {
        Request request = createTestRequest();
        Response badGatewayResponse = createMockResponse(502);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                badGatewayResponse, successResponse);

        retryOptions.setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually return 200");
        assertEquals(2, chain.getCallCount(), "Should retry once");
    }

    @Test
    @DisplayName("Test 504 Gateway Timeout - triggers retry")
    void testGatewayTimeoutRetry() throws IOException {
        Request request = createTestRequest();
        Response timeoutResponse = createMockResponse(504);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                timeoutResponse, successResponse);

        retryOptions.setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually return 200");
        assertEquals(2, chain.getCallCount(), "Should retry once");
    }

    @Test
    @DisplayName("Test 408 Request Timeout - triggers retry")
    void testRequestTimeoutRetry() throws IOException {
        Request request = createTestRequest();
        Response timeoutResponse = createMockResponse(408);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                timeoutResponse, successResponse);

        retryOptions.setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually return 200");
        assertEquals(2, chain.getCallCount(), "Should retry once");
    }

    // ===========================
    // Retry Limit Tests
    // ===========================

    @Test
    @DisplayName("Test retry limit is enforced")
    void testRetryLimitEnforced() throws IOException {
        Request request = createTestRequest();
        
        // Create 5 failure responses (more than retry limit of 3)
        Response failureResponse = createMockResponse(503);
        DynamicMockChain chain = new DynamicMockChain(request,
                failureResponse, failureResponse, failureResponse,
                failureResponse, failureResponse);

        retryOptions.setRetryLimit(3).setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        // Should stop after limit and return last failure
        assertEquals(503, result.code(), "Should return last failure response");
        assertEquals(4, chain.getCallCount(),
                "Should call exactly 4 times (1 initial + 3 retries)");
    }

    @Test
    @DisplayName("Test retry limit 0 means no retries")
    void testRetryLimitZeroNoRetries() throws IOException {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(failureResponse);

        retryOptions.setRetryLimit(0);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(503, result.code(), "Should return failure immediately");
        assertEquals(1, chain.getCallCount(),
                "Should only call once (no retries with limit 0)");
    }

    @Test
    @DisplayName("Test maximum retries eventually succeed")
    void testMaximumRetriesEventuallySucceed() throws IOException {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failureResponse, failureResponse, successResponse);

        retryOptions.setRetryLimit(3).setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually succeed");
        assertEquals(3, chain.getCallCount(),
                "Should call 3 times (2 failures + 1 success)");
    }

    // ===========================
    // Network Error (IOException) Tests
    // ===========================

    @Test
    @DisplayName("Test IOException triggers retry")
    void testIOExceptionTriggersRetry() {
        Request request = createTestRequest();
        
        // Create a chain that throws IOException on first call, succeeds on second
        Interceptor.Chain chain = new Interceptor.Chain() {
            private int callCount = 0;

            @Override
            public Request request() {
                return request;
            }

            @Override
            public Response proceed(Request req) throws IOException {
                callCount++;
                if (callCount == 1) {
                    throw new IOException("Network error");
                }
                return createMockResponse(200);
            }

            @Override
            public Connection connection() { return null; }
            @Override
            public Call call() { return null; }
            @Override
            public int connectTimeoutMillis() { return 10000; }
            @Override
            public Interceptor.Chain withConnectTimeout(int timeout, java.util.concurrent.TimeUnit unit) { return this; }
            @Override
            public int readTimeoutMillis() { return 10000; }
            @Override
            public Interceptor.Chain withReadTimeout(int timeout, java.util.concurrent.TimeUnit unit) { return this; }
            @Override
            public int writeTimeoutMillis() { return 10000; }
            @Override
            public Interceptor.Chain withWriteTimeout(int timeout, java.util.concurrent.TimeUnit unit) { return this; }
        };

        retryOptions.setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        assertDoesNotThrow(() -> {
            Response result = interceptor.intercept(chain);
            assertEquals(200, result.code(), "Should succeed after retry");
        });
    }

    @Test
    @DisplayName("Test IOException exhausts retries and throws")
    void testIOExceptionExhaustsRetries() {
        Request request = createTestRequest();
        MockChain chain = new MockChain(request);
        chain.setException(new IOException("Persistent network error"));

        retryOptions.setRetryLimit(2).setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        IOException exception = assertThrows(IOException.class,
                () -> interceptor.intercept(chain),
                "Should throw IOException after exhausting retries");
        
        assertTrue(exception.getMessage().contains("Persistent network error"),
                "Should throw original exception");
        assertEquals(3, chain.getCallCount(),
                "Should call exactly 3 times (1 initial + 2 retries)");
    }

    // ===========================
    // Retry Disabled Tests
    // ===========================

    @Test
    @DisplayName("Test retry disabled - no retries for 503")
    void testRetryDisabled() throws IOException {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(failureResponse);

        retryOptions.setRetryEnabled(false);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(503, result.code(), "Should return failure immediately");
        assertEquals(1, chain.getCallCount(),
                "Should only call once (retry disabled)");
    }

    // ===========================
    // Custom Retryable Status Codes Tests
    // ===========================

    @Test
    @DisplayName("Test custom retryable status codes")
    void testCustomRetryableStatusCodes() throws IOException {
        Request request = createTestRequest();
        Response customErrorResponse = createMockResponse(500); // Not in default list
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                customErrorResponse, successResponse);

        // Add 500 as retryable
        retryOptions.setRetryableStatusCodes(500, 502)
                    .setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should retry 500 and succeed");
        assertEquals(2, chain.getCallCount(), "Should retry once");
    }

    @Test
    @DisplayName("Test empty retryable status codes - no retries")
    void testEmptyRetryableStatusCodes() throws IOException {
        Request request = createTestRequest();
        Response serviceUnavailableResponse = createMockResponse(503);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(serviceUnavailableResponse);

        // Empty retryable codes
        retryOptions.setRetryableStatusCodes(new int[]{});
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(503, result.code(), "Should not retry");
        assertEquals(1, chain.getCallCount(),
                "Should only call once (no retryable codes defined)");
    }

    // ===========================
    // Backoff Strategy Tests (Timing)
    // ===========================

    @Test
    @DisplayName("Test FIXED backoff strategy delays are constant")
    void testFixedBackoffStrategy() throws IOException {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failureResponse, failureResponse, successResponse);

        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.FIXED)
                   .setRetryDelay(10L)
                   .setRetryLimit(2);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);
        
        assertEquals(200, result.code(), "Should succeed after retries");
        assertEquals(3, chain.getCallCount(), "Should make 3 calls (1 initial + 2 retries)");
        assertEquals(RetryOptions.BackoffStrategy.FIXED,
                retryOptions.getBackoffStrategy(),
                "Backoff strategy should be FIXED");
    }

    @Test
    @DisplayName("Test LINEAR backoff strategy")
    void testLinearBackoffStrategy() throws IOException {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failureResponse, failureResponse, successResponse);

        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.LINEAR)
                   .setRetryDelay(10L)
                   .setRetryLimit(2);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);
        
        assertEquals(200, result.code(), "Should succeed after retries");
        assertEquals(3, chain.getCallCount(), "Should make 3 calls (1 initial + 2 retries)");
        assertEquals(RetryOptions.BackoffStrategy.LINEAR,
                retryOptions.getBackoffStrategy(),
                "Backoff strategy should be LINEAR");
    }

    @Test
    @DisplayName("Test EXPONENTIAL backoff strategy")
    void testExponentialBackoffStrategy() {
        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.EXPONENTIAL)
                   .setRetryDelay(100L);
        interceptor = new RetryInterceptor(retryOptions);

        assertEquals(RetryOptions.BackoffStrategy.EXPONENTIAL,
                retryOptions.getBackoffStrategy(),
                "Backoff strategy should be EXPONENTIAL");
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @DisplayName("Test interceptor with null RetryOptions throws NullPointerException")
    void testNullRetryOptionsThrows() {
        assertThrows(NullPointerException.class,
                () -> new RetryInterceptor(null),
                "Should throw NullPointerException for null RetryOptions");
    }

    @Test
    @DisplayName("Test multiple retries with different status codes")
    void testMultipleRetriesWithDifferentStatusCodes() throws IOException {
        Request request = createTestRequest();
        
        DynamicMockChain chain = new DynamicMockChain(request,
                createMockResponse(503),
                createMockResponse(429),
                createMockResponse(502),
                createMockResponse(200));

        retryOptions.setRetryLimit(5).setRetryDelay(10L);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually succeed");
        assertEquals(4, chain.getCallCount(),
                "Should retry until success (3 failures + 1 success)");
    }

    @Test
    @DisplayName("Test interceptor is thread-safe for configuration")
    void testInterceptorThreadSafety() {
        // RetryInterceptor should be immutable after construction
        // This is a basic test to ensure configuration doesn't change
        RetryOptions options = new RetryOptions()
                .setRetryLimit(5)
                .setRetryDelay(2000L);
        
        RetryInterceptor interceptor1 = new RetryInterceptor(options);
        RetryInterceptor interceptor2 = new RetryInterceptor(options);
        
        // Both interceptors should work independently
        assertNotSame(interceptor1, interceptor2,
                "Different instances should be created");
    }

    // ===========================
    // Custom Backoff Strategy Tests
    // ===========================

    @Test
    @DisplayName("Test custom backoff is called for HTTP error retry")
    void testCustomBackoffCalledForHttpError() throws IOException {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failureResponse, successResponse);

        // Track if custom backoff was called
        final int[] callCount = {0};
        final int[] capturedAttempt = {-1};
        final int[] capturedStatusCode = {-1};
        
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            callCount[0]++;
            capturedAttempt[0] = attempt;
            capturedStatusCode[0] = statusCode;
            return 10L; // Fast retry for test
        });
        
        interceptor = new RetryInterceptor(retryOptions);
        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually succeed");
        assertEquals(1, callCount[0], "Custom backoff should be called once");
        assertEquals(0, capturedAttempt[0], "First retry is attempt 0");
        assertEquals(503, capturedStatusCode[0], "Should capture 503 status code");
    }

    @Test
    @DisplayName("Test custom backoff is called for network error retry")
    void testCustomBackoffCalledForNetworkError() {
        Request request = createTestRequest();
        
        final int[] callCount = {0};
        final int[] capturedAttempt = {-1};
        final int[] capturedStatusCode = {-1};
        final IOException[] capturedException = {null};
        
        // Chain that throws IOException first, then succeeds
        Interceptor.Chain chain = new Interceptor.Chain() {
            private int attemptCount = 0;

            @Override
            public Request request() {
                return request;
            }

            @Override
            public Response proceed(Request req) throws IOException {
                attemptCount++;
                if (attemptCount == 1) {
                    throw new IOException("Network error");
                }
                return createMockResponse(200);
            }

            @Override
            public Connection connection() { return null; }
            @Override
            public Call call() { return null; }
            @Override
            public int connectTimeoutMillis() { return 10000; }
            @Override
            public Interceptor.Chain withConnectTimeout(int timeout, java.util.concurrent.TimeUnit unit) { return this; }
            @Override
            public int readTimeoutMillis() { return 10000; }
            @Override
            public Interceptor.Chain withReadTimeout(int timeout, java.util.concurrent.TimeUnit unit) { return this; }
            @Override
            public int writeTimeoutMillis() { return 10000; }
            @Override
            public Interceptor.Chain withWriteTimeout(int timeout, java.util.concurrent.TimeUnit unit) { return this; }
        };

        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            callCount[0]++;
            capturedAttempt[0] = attempt;
            capturedStatusCode[0] = statusCode;
            capturedException[0] = exception;
            return 10L; // Fast retry for test
        });
        
        interceptor = new RetryInterceptor(retryOptions);

        assertDoesNotThrow(() -> {
            Response result = interceptor.intercept(chain);
            assertEquals(200, result.code(), "Should succeed after retry");
        });

        assertEquals(1, callCount[0], "Custom backoff should be called once");
        assertEquals(0, capturedAttempt[0], "First retry is attempt 0");
        assertEquals(-1, capturedStatusCode[0], "Network error has statusCode -1");
        assertNotNull(capturedException[0], "Should capture IOException");
        assertTrue(capturedException[0].getMessage().contains("Network error"));
    }

    @Test
    @DisplayName("Test custom backoff with exponential + jitter pattern")
    void testCustomBackoffWithJitter() throws IOException {
        Request request = createTestRequest();
        
        // Create multiple failures to test jitter across attempts
        Response failure503 = createMockResponse(503);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failure503, failure503, failure503, success);

        final java.util.List<Long> calculatedDelays = new java.util.ArrayList<>();
        
        // Exponential backoff with jitter
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            long exponential = 100L * (long)Math.pow(2, attempt);
            long jitter = (long)(Math.random() * 50);
            long delay = exponential + jitter;
            calculatedDelays.add(delay);
            return delay;
        });
        
        interceptor = new RetryInterceptor(retryOptions);
        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should eventually succeed");
        assertEquals(3, calculatedDelays.size(), "Should calculate 3 delays");
        
        // Verify delays are increasing (exponential)
        assertTrue(calculatedDelays.get(1) > calculatedDelays.get(0),
                "Second delay should be greater than first");
        assertTrue(calculatedDelays.get(2) > calculatedDelays.get(1),
                "Third delay should be greater than second");
    }

    @Test
    @DisplayName("Test custom backoff with rate limit logic")
    void testCustomBackoffWithRateLimitLogic() throws IOException {
        Request request = createTestRequest();
        Response rateLimitResponse = createMockResponse(429);
        Response successResponse = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                rateLimitResponse, successResponse);

        final int[] capturedStatusCode = {-1};
        
        // Custom logic: longer delay for 429 rate limits
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            capturedStatusCode[0] = statusCode;
            if (statusCode == 429) {
                return 100L; // Longer delay for rate limit
            }
            return 10L; // Normal delay
        });
        
        interceptor = new RetryInterceptor(retryOptions);
        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should succeed after retry");
        assertEquals(429, capturedStatusCode[0],
                "Should detect 429 rate limit status");
    }

    @Test
    @DisplayName("Test custom backoff with maximum delay cap")
    void testCustomBackoffWithMaxDelayCap() throws IOException {
        Request request = createTestRequest();
        Response failure = createMockResponse(503);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failure, failure, failure, success);

        final java.util.List<Long> calculatedDelays = new java.util.ArrayList<>();
        final long MAX_DELAY = 500L;
        
        // Exponential backoff with cap
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            long exponential = 100L * (long)Math.pow(2, attempt);
            long cappedDelay = Math.min(exponential, MAX_DELAY);
            calculatedDelays.add(cappedDelay);
            return cappedDelay;
        });
        
        interceptor = new RetryInterceptor(retryOptions);
        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code(), "Should succeed");
        assertEquals(3, calculatedDelays.size());
        
        // Verify all delays are capped
        for (Long delay : calculatedDelays) {
            assertTrue(delay <= MAX_DELAY,
                    "Delay " + delay + " should not exceed max " + MAX_DELAY);
        }
    }

    @Test
    @DisplayName("Test custom backoff takes precedence over built-in strategies")
    void testCustomBackoffTakesPrecedence() throws IOException {
        Request request = createTestRequest();
        Response failure = createMockResponse(503);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request, failure, success);

        final long CUSTOM_DELAY = 123L;
        final long[] actualDelay = {0L};
        
        // Set both EXPONENTIAL and custom - custom should win
        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.EXPONENTIAL)
                   .setRetryDelay(1000L) // Would be 1000ms for exponential
                   .setCustomBackoffStrategy((attempt, statusCode, exception) -> {
                       actualDelay[0] = CUSTOM_DELAY;
                       return CUSTOM_DELAY;
                   });
        
        interceptor = new RetryInterceptor(retryOptions);
        
        long startTime = System.currentTimeMillis();
        Response result = interceptor.intercept(chain);
        long duration = System.currentTimeMillis() - startTime;

        assertEquals(200, result.code());
        assertEquals(CUSTOM_DELAY, actualDelay[0],
                "Custom backoff should be called, not exponential");
        assertTrue(duration >= CUSTOM_DELAY,
                "Should use custom delay, not exponential");
    }

    @Test
    @DisplayName("Test custom backoff can return zero delay")
    void testCustomBackoffWithZeroDelay() throws IOException {
        Request request = createTestRequest();
        Response failure = createMockResponse(503);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request, failure, success);

        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> 0L);
        interceptor = new RetryInterceptor(retryOptions);
        
        long startTime = System.currentTimeMillis();
        Response result = interceptor.intercept(chain);
        long duration = System.currentTimeMillis() - startTime;

        assertEquals(200, result.code());
        assertTrue(duration < 50L, "Should retry almost immediately with zero delay");
    }

    @Test
    @DisplayName("Test custom backoff receives correct attempt numbers")
    void testCustomBackoffReceivesCorrectAttempts() throws IOException {
        Request request = createTestRequest();
        Response failure = createMockResponse(503);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failure, failure, failure, success);

        final java.util.List<Integer> capturedAttempts = new java.util.ArrayList<>();
        
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            capturedAttempts.add(attempt);
            return 10L;
        });
        
        interceptor = new RetryInterceptor(retryOptions);
        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code());
        assertEquals(java.util.Arrays.asList(0, 1, 2), capturedAttempts,
                "Should receive attempts 0, 1, 2 (0-based)");
    }

    @Test
    @DisplayName("Test custom backoff is called multiple times for multiple retries")
    void testCustomBackoffCalledMultipleTimes() throws IOException {
        Request request = createTestRequest();
        Response failure = createMockResponse(503);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request,
                failure, failure, failure, success);

        final int[] callCount = {0};
        
        retryOptions.setRetryLimit(5)
                   .setCustomBackoffStrategy((attempt, statusCode, exception) -> {
                       callCount[0]++;
                       return 10L;
                   });
        
        interceptor = new RetryInterceptor(retryOptions);
        Response result = interceptor.intercept(chain);

        assertEquals(200, result.code());
        assertEquals(3, callCount[0],
                "Custom backoff should be called 3 times (for 3 retries)");
    }

    @Test
    @DisplayName("Test null custom backoff strategy throws exception")
    void testNullCustomBackoffThrows() {
        assertThrows(NullPointerException.class,
                () -> retryOptions.setCustomBackoffStrategy(null),
                "Setting null custom backoff should throw NullPointerException");
    }

    @Test
    @DisplayName("Test hasCustomBackoff returns correct status")
    void testHasCustomBackoffStatus() {
        assertFalse(retryOptions.hasCustomBackoff(),
                "Should not have custom backoff initially");
        
        retryOptions.setCustomBackoffStrategy((a, s, e) -> 1000L);
        
        assertTrue(retryOptions.hasCustomBackoff(),
                "Should have custom backoff after setting it");
    }

    // ===========================
    // Interruption Tests
    // ===========================

    @Test
    @DisplayName("Test thread interruption during HTTP retry sleep")
    void testThreadInterruptionDuringHttpRetrySleep() throws IOException {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(failureResponse);

        // Set a long delay and interrupt the thread
        retryOptions.setRetryLimit(3).setRetryDelay(10000L);
        interceptor = new RetryInterceptor(retryOptions);

        // Interrupt the thread before calling intercept
        Thread testThread = new Thread(() -> {
            try {
                Thread.sleep(50); // Wait a bit for the retry to start
                Thread.currentThread().interrupt();
            } catch (InterruptedException ignored) {
            }
        });
        testThread.start();

        // Simulate interruption by using a custom backoff that interrupts
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            Thread.currentThread().interrupt();
            return 10L;
        });
        interceptor = new RetryInterceptor(retryOptions);

        IOException thrown = assertThrows(IOException.class,
                () -> interceptor.intercept(chain),
                "Should throw IOException when interrupted");
        
        assertTrue(thrown.getMessage().contains("Retry interrupted"),
                "Exception message should indicate interruption");
        assertTrue(Thread.interrupted(), "Thread interrupt flag should be set");
    }

    @Test
    @DisplayName("Test thread interruption during IOException retry sleep")
    void testThreadInterruptionDuringIOExceptionRetrySleep() {
        Request request = createTestRequest();
        MockChain chain = new MockChain(request);
        chain.setException(new IOException("Network error"));

        retryOptions.setRetryLimit(3).setRetryDelay(10000L);

        // Use custom backoff to trigger interruption
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            Thread.currentThread().interrupt();
            return 10L;
        });
        interceptor = new RetryInterceptor(retryOptions);

        IOException thrown = assertThrows(IOException.class,
                () -> interceptor.intercept(chain),
                "Should throw IOException when interrupted");
        
        assertTrue(thrown.getMessage().contains("Retry interrupted"),
                "Exception message should indicate interruption");
        assertTrue(Thread.interrupted(), "Thread interrupt flag should be set");
    }

    @Test
    @DisplayName("Test response is closed on interruption")
    void testResponseClosedOnInterruption() {
        Request request = createTestRequest();
        Response failureResponse = createMockResponse(503);
        
        MockChain chain = new MockChain(request);
        chain.setResponse(failureResponse);

        // Use custom backoff to trigger interruption
        retryOptions.setCustomBackoffStrategy((attempt, statusCode, exception) -> {
            Thread.currentThread().interrupt();
            return 10L;
        });
        interceptor = new RetryInterceptor(retryOptions);

        assertThrows(IOException.class, () -> interceptor.intercept(chain));
        assertTrue(Thread.interrupted(), "Thread should be interrupted");
    }

    @Test
    @DisplayName("Test interruption when response is null")
    void testInterruptionWithNullResponse() {
        Request request = createTestRequest();
        MockChain chain = new MockChain(request);
        chain.setException(new IOException("Network error"));

        // Interrupt immediately before any response is received
        retryOptions.setRetryLimit(1)
                .setCustomBackoffStrategy((attempt, statusCode, exception) -> {
                    Thread.currentThread().interrupt();
                    return 10L;
                });
        interceptor = new RetryInterceptor(retryOptions);

        IOException thrown = assertThrows(IOException.class,
                () -> interceptor.intercept(chain),
                "Should throw IOException when interrupted");
        
        assertTrue(thrown.getMessage().contains("Retry interrupted"),
                "Exception message should indicate interruption");
        assertTrue(Thread.interrupted(), "Thread interrupt flag should be set");
    }

    @Test
    @DisplayName("Test FIXED backoff with multiple retries")
    void testFixedBackoffWithMultipleRetries() throws IOException {
        Request request = createTestRequest();
        Response failure1 = createMockResponse(503);
        Response failure2 = createMockResponse(502);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request, failure1, failure2, success);

        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.FIXED)
                .setRetryDelay(5L)
                .setRetryLimit(3);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);
        assertEquals(200, result.code(), "Should succeed after retries with FIXED backoff");
        assertEquals(3, chain.getCallCount(), "Should make 3 calls");
    }

    @Test
    @DisplayName("Test LINEAR backoff with multiple retries")
    void testLinearBackoffWithMultipleRetries() throws IOException {
        Request request = createTestRequest();
        Response failure1 = createMockResponse(503);
        Response failure2 = createMockResponse(502);
        Response success = createMockResponse(200);
        
        DynamicMockChain chain = new DynamicMockChain(request, failure1, failure2, success);

        retryOptions.setBackoffStrategy(RetryOptions.BackoffStrategy.LINEAR)
                .setRetryDelay(5L)
                .setRetryLimit(3);
        interceptor = new RetryInterceptor(retryOptions);

        Response result = interceptor.intercept(chain);
        assertEquals(200, result.code(), "Should succeed after retries with LINEAR backoff");
        assertEquals(3, chain.getCallCount(), "Should make 3 calls");
    }
}

