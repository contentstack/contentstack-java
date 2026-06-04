package com.contentstack.sdk;

import java.util.Map;
import java.util.Objects;

/**
 * The Content Delivery API is used to retrieve content from your Contentstack
 * account and deliver it to your web or
 * mobile properties. If you are looking for APIs to manage content, you should
 * use the Content Management API
 * <p>
 * Our APIs serve content via a powerful and robust content delivery network
 * (CDN). Multiple datacenters around the
 * world store a cached copy of your content. When a page request is made, the
 * content is delivered to the user from the
 * nearest server. This greatly accelerates content delivery and reduces
 * latency.
 */
public class Contentstack {

    // Modifier Protected
    protected Contentstack() throws IllegalAccessException {
        throw new IllegalAccessException(ErrorMessages.DIRECT_INSTANTIATION_CONTENTSTACK);
    }

    /**
     * A stack is a space that stores the content of a project (a web or mobile
     * property). Within a stack, you can
     * create content structures, content entries, users, etc. related to the
     * project. <br>
     * Authenticates the stack api
     * key of your stack. <br>
     * Find Your Stack Credentials from Contentstack .
     *
     * @param stackApiKey   The API Key is a unique key assigned to each stack.
     * @param deliveryToken The Delivery Token is a read-only credential that you
     *                      can create for different environments of your
     *                      stack
     * @param environment   the environment for the stack
     * @return the stack
     * @throws IllegalAccessException the illegal access exception
     *                                <p>
     *                                <b>Example</b>
     *
     *                                <pre>
     *                                                                                              {
     *                                                                                                  &#64;Code
     *                                                                                                  Stack stack = contentstack.Stack("apiKey", "deliveryToken", "environment");
     *                                                                                              }
     *
     *                                                                                              </pre>
     */
    public static Stack stack(String stackApiKey, String deliveryToken, String environment)
            throws IllegalAccessException {
        validateCredentials(stackApiKey, deliveryToken, environment);
        Config config = new Config();
        return initializeStack(stackApiKey, deliveryToken, environment, config);
    }

    /**
     * A stack is a space that stores the content of a project (a web or mobile
     * property). Within a stack, you can
     * create content structures, content entries, users, etc. related to the
     * project.
     *
     * @param stackApiKey   The API Key is a unique key assigned to each stack.
     * @param deliveryToken The Delivery Token is a read-only credential that you
     *                      can create for different environments of your
     *                      stack
     * @param environment   the environment for the stack
     * @param config        the config
     * @return the stack
     * @throws IllegalAccessException the illegal access exception <b>Example</b>
     *                                <p>
     *                                { @Code Stack stack =
     *                                contentstack.Stack("apiKey", "deliveryToken",
     *                                "environment"); }
     */
    public static Stack stack(String stackApiKey, String deliveryToken, String environment, Config config)
            throws IllegalAccessException {
        validateCredentials(stackApiKey, deliveryToken, environment);
        return initializeStack(stackApiKey, deliveryToken, environment, config);
    }

    private static void validateCredentials(String stackApiKey, String deliveryToken, String environment)
            throws IllegalAccessException {
        Objects.requireNonNull(stackApiKey, "API Key can not be null");
        Objects.requireNonNull(deliveryToken, "Delivery Token can not be null");
        Objects.requireNonNull(environment, "Environment can not be null");

        if (stackApiKey.isEmpty()) {
            throw new IllegalAccessException(ErrorMessages.MISSING_API_KEY);
        }
        if (deliveryToken.isEmpty()) {
            throw new IllegalAccessException(ErrorMessages.MISSING_DELIVERY_TOKEN);
        }
        if (environment.isEmpty()) {
            throw new IllegalAccessException(ErrorMessages.MISSING_ENVIRONMENT);
        }
    }

    /**
     * Returns the Contentstack API URL for the given region and service.
     *
     * <p>Delegates to {@link Endpoint#getContentstackEndpoint(String, String)} — provided as a
     * convenience so callers can reach endpoint resolution through the same top-level class they
     * use to create stacks.
     *
     * @param region  region ID or alias (e.g. {@code "na"}, {@code "eu"}, {@code "azure-na"})
     * @param service service key (e.g. {@code "contentDelivery"}, {@code "contentManagement"})
     * @return full URL including {@code https://} scheme
     * @throws IllegalArgumentException if the region or service is not recognised
     */
    public static String getContentstackEndpoint(String region, String service) {
        return Endpoint.getContentstackEndpoint(region, service);
    }

    /**
     * Returns the Contentstack API URL for the given region and service, optionally stripping
     * the {@code https://} scheme.
     *
     * @param region      region ID or alias
     * @param service     service key
     * @param omitHttps   when {@code true}, returns the bare host without {@code https://}
     * @return URL or bare host
     * @throws IllegalArgumentException if the region or service is not recognised
     */
    public static String getContentstackEndpoint(String region, String service, boolean omitHttps) {
        return Endpoint.getContentstackEndpoint(region, service, omitHttps);
    }

    /**
     * Returns all service endpoints for the given region as an ordered map of service key to URL.
     *
     * @param region region ID or alias
     * @return map of service key → full URL
     * @throws IllegalArgumentException if the region is not recognised
     */
    public static Map<String, String> getContentstackEndpoints(String region) {
        return Endpoint.getAllEndpoints(region);
    }

    /**
     * Returns all service endpoints for the given region, optionally stripping the
     * {@code https://} scheme from every URL.
     *
     * @param region    region ID or alias
     * @param omitHttps when {@code true}, returns bare hosts without {@code https://}
     * @return map of service key → URL or bare host
     * @throws IllegalArgumentException if the region is not recognised
     */
    public static Map<String, String> getContentstackEndpoints(String region, boolean omitHttps) {
        return Endpoint.getAllEndpoints(region, omitHttps);
    }

    private static Stack initializeStack(String stackApiKey, String deliveryToken, String environment, Config config) {
        Stack stack = new Stack(stackApiKey.trim());
        stack.setHeader("api_key", stackApiKey);
        stack.setHeader("access_token", deliveryToken);
        stack.setHeader("environment", environment);
        if (config.getBranch() != null && !config.getBranch().isEmpty()) {
            stack.setHeader("branch", config.getBranch());
        }
        if (config.getEarlyAccess() != null && config.getEarlyAccess().length > 0) {
            String eaValues = String.join(",", config.earlyAccess).replace("\"", "");
            stack.setHeader("x-header-ea", eaValues);
        }
        stack.setConfig(config);
        return stack;
    }

}
