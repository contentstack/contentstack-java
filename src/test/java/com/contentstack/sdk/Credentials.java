package com.contentstack.sdk;
import io.github.cdimascio.dotenv.Dotenv;

import java.rmi.AccessException;

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


    public final static String HOST = (env.get("HOST") != null) ? env.get("HOST") : "cdn.contentstack.io";
    public final static String API_KEY = (env.get("API_KEY") != null) ? env.get("API_KEY") : "";
    public final static String DELIVERY_TOKEN = (env.get("DELIVERY_TOKEN") != null) ? env.get("DELIVERY_TOKEN") : "";
    public final static String ENVIRONMENT = (env.get("ENVIRONMENT") != null) ? env.get("ENVIRONMENT") : "env1";
    public final static String CONTENT_TYPE = (env.get("contentType") != null) ? env.get("contentType") : "product";
    public final static String ENTRY_UID = (env.get("assetUid") != null) ? env.get("assetUid") : "";


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
