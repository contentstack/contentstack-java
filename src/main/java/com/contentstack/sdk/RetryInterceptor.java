package com.contentstack.sdk;

import java.io.IOException;
import java.util.Arrays;
import java.util.logging.Logger;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class RetryInterceptor implements Interceptor {
    
    private static final Logger logger = Logger.getLogger(RetryInterceptor.class.getName());
    private final RetryOptions retryOptions;


    public RetryInterceptor(RetryOptions retryOptions) {
        if (retryOptions == null) {
            throw new NullPointerException("RetryOptions cannot be null");
        }
        this.retryOptions = retryOptions;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response = null;
        IOException lastException = null;
        
        // If retry is disabled, just proceed with the request once
        if (!retryOptions.isRetryEnabled()) {
            return chain.proceed(request);
        }
        
        int attempt = 0;
        // retryLimit means number of retries, so total attempts = 1 initial + retryLimit retries
        int maxAttempts = retryOptions.getRetryLimit() + 1;

        while (attempt < maxAttempts) {
            
            try {
                if(response != null) {
                    response.close();
                }
                response = chain.proceed(request);
               
                if (shouldRetry(response.code()) && (attempt + 1) < maxAttempts) {
                    logger.fine("Retry attempt " + (attempt + 1) + " for status " + response.code() + " on " + request.url());
                    
                    long delay = calculateDelay(attempt, response.code(), null);
                    Thread.sleep(delay);
                    attempt++;
                    continue;
                }
                return response;

            } catch (IOException e) {
                // Network error occurred
                lastException = e;
                
                if ((attempt + 1) < maxAttempts) {
                    try {
                        long delay = calculateDelay(attempt, -1, e);
                        Thread.sleep(delay);
                        attempt++;
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new IOException("Retry interrupted", ie);
                    }
                    continue;
                } else {
                    // No more retries, throw the exception
                    throw e;
                }
                
            } catch (InterruptedException e) {
                // Thread was interrupted during sleep
                Thread.currentThread().interrupt();
                if (response != null) response.close();
                throw new IOException("Retry interrupted", e);
            }
        }
        
        // Should not reach here normally
        if (lastException != null) {
            throw lastException;
        }
        return response;
    }
    
    /**
     * Determines if a status code should trigger a retry.
     * 
     * @param statusCode HTTP status code
     * @return true if this status code is retryable
     */
    private boolean shouldRetry(int statusCode) {
        return Arrays.stream(retryOptions.getRetryableStatusCodes()).anyMatch(code -> code == statusCode);
    }
    
    /**
     * Calculates the delay before the next retry attempt.
     * 
     * @param attempt current attempt number (0-based)
     * @param statusCode HTTP status code (-1 for network errors)
     * @param exception the IOException that occurred (may be null)
     * @return delay in milliseconds
     */
    private long calculateDelay(int attempt, int statusCode, IOException exception) {

        if(retryOptions.hasCustomBackoff()) {
            return retryOptions.getCustomBackoffStrategy().calculateDelay(attempt, statusCode, exception);
        }
        long baseDelay = retryOptions.getRetryDelay();
  
        switch (retryOptions.getBackoffStrategy()) {
            case FIXED:
                // baseDelay already set above
                break;
                
            case LINEAR:
                baseDelay = retryOptions.getRetryDelay() * (attempt + 1);
                break;
                
            case EXPONENTIAL:
                baseDelay = (long) (retryOptions.getRetryDelay() * Math.pow(2,attempt));
                break;
                
            default:
                // baseDelay already set above
                break;
        }
        
        return baseDelay;
    }

}
