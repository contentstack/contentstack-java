package com.contentstack.sdk;

import io.github.cdimascio.dotenv.Dotenv;
import io.github.cdimascio.dotenv.DotenvException;

import java.io.File;
import java.io.IOException;
import java.rmi.AccessException;

public class Credentials {
    static Dotenv env = getEnv();

    /**
     * The provided Java code defines a method named `getEnv()` that attempts to load environment variables from a `.env` file using the `Dotenv` library. If loading the environment variables encounters an exception (specifically, a `DotenvException`), the code takes an alternative path by creating an empty `.env` file in the current working directory.
     * <p>
     * Here's a breakdown of what the code is doing step by step:
     * <p>
     * 1. The method `public static Dotenv getEnv()` is defined. It returns an instance of the `Dotenv` class, which is used to manage environment variables loaded from the `.env` file.
     * <p>
     * 2. Inside the `try` block, the code tries to load environment variables using `Dotenv.load()`. If successful, the loaded environment variables are stored in the `env` variable.
     * <p>
     * 3. If loading the environment variables from the `.env` file encounters an exception (a `DotenvException`), the code enters the `catch` block.
     * <p>
     * 4. In the `catch` block, it gets the current working directory using `System.getProperty("user.dir")` and creates a `File` object named `envFile` representing the `.env` file in the current directory.
     * <p>
     * 5. The code attempts to create an empty `.env` file using `envFile.createNewFile()`.
     * <p>
     * 6. If there's an error during the file creation process (an `IOException`), an error message is printed to the standard error output, and the exception's stack trace is printed for debugging purposes.
     * <p>
     * 7. Finally, regardless of whether the environment variables were successfully loaded or a new `.env` file was created, the method returns the `env` variable, which may either contain the loaded environment variables or be `null` if an exception occurred during the loading process.
     * <p>
     * In summary, this code defines a method that attempts to load environment variables from a `.env` file using the `Dotenv` library. If loading fails due to an exception, it creates an empty `.env` file in the current working directory and then returns the `Dotenv` instance, which may or may not have loaded environment variables depending on whether an exception occurred.
     *
     * @return Dotenv
     */
    public static Dotenv getEnv() {
        try {
            env = Dotenv.load();
        } catch (DotenvException ex) {
            String currentDirectory = System.getProperty("user.dir");
            File envFile = new File(currentDirectory, ".env");
            try {
                // Create .env file in the current directory
                envFile.createNewFile();
            } catch (IOException e) {
                System.err.println("An error occurred while creating .env file.");
                e.printStackTrace();
            }
        }
        return env;
    }


    public final static String pwd = (env.get("PWD") != null) ? env.get("PWD") : "contentstack-java";
    public final static String HOST = (env.get("HOST") != null) ? env.get("HOST") : "cdn.contentstack.io";
    public final static String API_KEY = (env.get("API_KEY") != null) ? env.get("API_KEY") : "blt12c8ad610ff4ddc2";
    public final static String DELIVERY_TOKEN = (env.get("DELIVERY_TOKEN") != null) ? env.get("DELIVERY_TOKEN") : "cs9c1afdffa298f8708e3459e4";
    public final static String ENVIRONMENT = (env.get("ENVIRONMENT") != null) ? env.get("ENVIRONMENT") : "env1";
    public final static String CONTENT_TYPE = (env.get("contentType") != null) ? env.get("contentType") : "product";
    public final static String ENTRY_UID = (env.get("assetUid") != null) ? env.get("assetUid") : "blt884786476373";

    private static Stack stack;

    private Credentials() throws AccessException {
        // Private constructor to prevent direct instantiation
        throw new AccessException("Can not access credential access");
    }

    public static Stack getStack() {

        if (stack == null) {
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
