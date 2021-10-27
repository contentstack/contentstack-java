package com.contentstack.sdk;

import java.util.Objects;

/**
 * The Content Delivery API is used to retrieve content from your Contentstack
 * account and deliver it to your web or mobile properties. If you are looking
 * for APIs to manage content, you should use the Content Management API
 * <p>
 * Our APIs serve content via a powerful and robust content delivery network
 * (CDN). Multiple datacenters around the world store a cached copy of your
 * content. When a page request is made, the content is delivered to the user
 * from the nearest server. This greatly accelerates content delivery and
 * reduces latency.
 */
public class Contentstack {

    // Modifier Protected
    protected Contentstack() throws IllegalAccessException {
        throw new IllegalAccessException("Can Not Access Private Modifier");
    }

    /**
     * A stack is a space that stores the content of a project (a web or mobile
     * property). Within a stack, you can create content structures, content
     * entries, users, etc. related to the project. <br>
     * Authenticates the stack api key of your stack. <br>
     * Find Your Stack Credentials from Contentstack .
     *
     * @param stackApiKey   The API Key is a unique key assigned to each stack.
     * @param deliveryToken The Delivery Token is a read-only credential that you
     *                      can create for different environments of your stack
     * @param environment   the environment for the stack
     * @return the stack
     * @throws IllegalAccessException the illegal access exception
     *
     *                                <b>Example</b>
     * 
     *                                <pre>
     *                                {
     *                                    &#64;Code
     *                                    Stack stack = contentstack.Stack("apiKey", "deliveryToken", "environment");
     *                                }
     *
     *                                </pre>
     */
    public static Stack stack(String stackApiKey, String deliveryToken, String environment)
            throws IllegalAccessException {
        validateCredentials(stackApiKey, deliveryToken, environment);
        Config config = new Config();
        return initializeStack(stackApiKey, deliveryToken, environment, config);
    }

    /**
     * A stack is a space that stores the content of a project (a web or mobile
     * property). Within a stack, you can create content structures, content
     * entries, users, etc. related to the project.
     *
     * @param stackApiKey   The API Key is a unique key assigned to each stack.
     * @param deliveryToken The Delivery Token is a read-only credential that you
     *                      can create for different environments of your stack
     * @param environment   the environment for the stack
     * @param config        the config
     * @return the stack
     * @throws IllegalAccessException the illegal access exception <b>Example</b>
     * 
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
            throw new IllegalAccessException("API Key can not be empty");
        }
        if (deliveryToken.isEmpty()) {
            throw new IllegalAccessException("Delivery Token can not be empty");
        }
        if (environment.isEmpty()) {
            throw new IllegalAccessException("Environment can not be empty");
        }
    }

    private static Stack initializeStack(String stackApiKey, String deliveryToken, String environment, Config config) {
        Stack stack = new Stack(stackApiKey.trim());
        stack.setHeader("api_key", stackApiKey);
        stack.setHeader("access_token", deliveryToken);
        stack.setHeader("environment", environment);
        stack.setConfig(config);
        return stack;
    }

}
