package com.contentstack.sdk;

import java.rmi.AccessException;
import java.util.Arrays;
import io.github.cdimascio.dotenv.Dotenv;

public class Credentials {
   
    static Dotenv env = getEnv();

    private static String envChecker() {
        String githubActions = System.getenv("GITHUB_ACTIONS");
        if (githubActions != null && githubActions.equals("true")) {
            return "GitHub";
        } else {
            return "local";
        }
    }

    public static Dotenv getEnv() {
         env = Dotenv.configure()
                 .directory("src/test/resources")
                 .filename("env") // instead of '.env', use 'env'
                 .load();

         return Dotenv.load();
     }

    public static final String HOST = env.get("HOST", "cdn.contentstack.io");
    public static final String API_KEY = env.get("API_KEY", "");
    public static final String DELIVERY_TOKEN = env.get("DELIVERY_TOKEN", "");
    public static final String ENVIRONMENT = env.get("ENVIRONMENT", "env1");
    public static final String CONTENT_TYPE = env.get("contentType", "product");
    public static final String ENTRY_UID = env.get("assetUid", "");
    public static final String VARIANT_UID = env.get("variantUid", "");
    public final static String[] VARIANTS_UID;
    static {
        String variantsUidString = env.get("variantsUid");

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
