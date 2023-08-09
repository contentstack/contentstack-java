package com.contentstack.sdk;

import lombok.var;

import java.rmi.AccessException;

public class Credentials {
    //static Dotenv env = getEnv();

    private static String envChecker() {
        String githubActions = System.getenv("GITHUB_ACTIONS");
        if (githubActions != null && githubActions.equals("true")) {
            System.out.println("Tests are running in GitHub Actions environment.");
            String mySecretKey = System.getenv("API_KEY");
            System.out.println("My Secret Key: " + mySecretKey);
            return "GitHub";
        } else {
            System.out.println("Tests are running in a local environment.");
            return "local";
        }
    }

//    public static Dotenv getEnv() {
//        String currentDirectory = System.getProperty("user.dir");
//        File envFile = new File(currentDirectory, "env");
//        env = Dotenv.configure()
//                .directory("src/test/resources")
//                .filename("env") // instead of '.env', use 'env'
//                .load();
//        try {
//            env = Dotenv.load();
//        } catch (DotenvException ex) {
//            System.out.println("Could not load from local .env");
////            File envFile = new File(currentDirectory, ".env");
////            try {
////                // Create .env file in the current directory
////                envFile.createNewFile();
////            } catch (IOException e) {
////                System.err.println("An error occurred while creating .env file.");
////                e.printStackTrace();
////            }
//        }
//        return env;
//    }


//    public final static String pwd = (env.get("PWD") != null) ? env.get("PWD") : "contentstack-java";
//    public final static String HOST = (env.get("HOST") != null) ? env.get("HOST") : "cdn.contentstack.io";
//    public final static String API_KEY = (env.get("API_KEY") != null) ? env.get("API_KEY") : "***REMOVED***";
//    public final static String DELIVERY_TOKEN = (env.get("DELIVERY_TOKEN") != null) ? env.get("DELIVERY_TOKEN") : "***REMOVED***";
//    public final static String ENVIRONMENT = (env.get("ENVIRONMENT") != null) ? env.get("ENVIRONMENT") : "env1";
//    public final static String CONTENT_TYPE = (env.get("contentType") != null) ? env.get("contentType") : "product";
//    public final static String ENTRY_UID = (env.get("assetUid") != null) ? env.get("assetUid") : "blt884786476373";


    public final static String HOST = "cdn.contentstack.io";
    public final static String API_KEY = "***REMOVED***";
    public final static String DELIVERY_TOKEN = "***REMOVED***";
    public final static String ENVIRONMENT = "env1";
    public final static String CONTENT_TYPE = "product";
    public final static String ENTRY_UID = "blt884786476373";

    private static volatile Stack stack;

    private Credentials() throws AccessException {
        throw new AccessException("Can not access credential access");
    }

    public static Stack getStack() {
        if (stack == null) {
            var envCheck = envChecker();
            System.out.println(envCheck);
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
