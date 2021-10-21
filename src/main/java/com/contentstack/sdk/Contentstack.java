package com.contentstack.sdk;

import java.util.Objects;


public class Contentstack {


    // Modifier Protected
    protected Contentstack() throws IllegalAccessException {
        throw new IllegalAccessException("Can Not Access Private Modifier");
    }


    /**
     * Authenticates the stack api key of your stack.
     * <br>
     * Find Your Stack Credentials from Contentstack .
     *
     * @param stackApiKey
     *         the stack api key
     * @param deliveryToken
     *         the delivery token
     * @param environment
     *         the environment
     * @return the stack
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    public static Stack stack(String stackApiKey,
                              String deliveryToken,
                              String environment) throws IllegalAccessException {
        validateCredentials(stackApiKey, deliveryToken, environment);
        Config config = new Config();
        return initializeStack(stackApiKey, deliveryToken, environment, config);
    }


    /**
     * Authenticates the stack api key of your stack.
     * <br>
     * You can find your stack api key from web.
     *
     * @param stackApiKey
     *         the stack api key
     * @param deliveryToken
     *         the delivery token
     * @param environment
     *         the environment
     * @param config
     *         the config
     * @return the stack
     * @throws IllegalAccessException
     *         the illegal access exception
     */
    public static Stack stack(String stackApiKey,
                              String deliveryToken,
                              String environment,
                              Config config) throws IllegalAccessException {
        validateCredentials(stackApiKey, deliveryToken, environment);
        return initializeStack(stackApiKey, deliveryToken, environment, config);
    }

    private static void validateCredentials(String stackApiKey,
                                            String deliveryToken,
                                            String environment) throws IllegalAccessException {
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
