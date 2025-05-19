package com.contentstack.sdk;

import java.io.FileInputStream;
import java.io.IOException;
import java.rmi.AccessException;
import java.util.Arrays;
import java.util.Properties;

public class Credentials {
    private static final Properties properties = new Properties();

    private static String envChecker() {
        String githubActions = System.getenv("GITHUB_ACTIONS");
        if (githubActions != null && githubActions.equals("true")) {
            return "GitHub";
        } else {
            return "local";
        }
    }

    static {
        try (FileInputStream inputStream = new FileInputStream("src/test/resources/test-config.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            System.err.println("Error loading properties file: " + e.getMessage());
        }
    }

    public static final String HOST = properties.getProperty("HOST", "cdn.contentstack.io");
    public static final String API_KEY = properties.getProperty("API_KEY", "");
    public static final String DELIVERY_TOKEN = properties.getProperty("DELIVERY_TOKEN", "");
    public static final String ENVIRONMENT = properties.getProperty("ENVIRONMENT", "env1");
    public static final String CONTENT_TYPE = properties.getProperty("contentType", "product");
    public static final String ENTRY_UID = properties.getProperty("assetUid", "");
    public static final String VARIANT_UID = properties.getProperty("variantUid", "");
    public final static String[] VARIANTS_UID;
    static {
        String variantsUidString = properties.getProperty("variantsUid");

        if (variantsUidString != null && !variantsUidString.trim().isEmpty()) {
            VARIANTS_UID = Arrays.stream(variantsUidString.split(","))
                    .map(String::trim)
                    .toArray(String[]::new);
        } else {
            VARIANTS_UID = new String[] {};
        }
    }

    private static volatile Stack stack;

    private Credentials() throws AccessException {
        throw new AccessException("Can not access");
    }

    public static Stack getStack() {
        if (stack == null) {
            envChecker();
            synchronized (Credentials.class) {
                if (stack == null) {
                    try {
                        Config config = new Config();
                        config.setHost(HOST);
                        stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENVIRONMENT, config);
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
        return stack;
    }

}
