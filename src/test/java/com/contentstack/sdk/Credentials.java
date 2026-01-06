package com.contentstack.sdk;

import java.rmi.AccessException;
import java.util.Arrays;
import io.github.cdimascio.dotenv.Dotenv;

public class Credentials {
   
    static Dotenv env = Dotenv.configure()
                            .directory("src/test/resources")
                            .filename(".env")
                            .load();


    private static String envChecker() {
        String githubActions = System.getenv("GITHUB_ACTIONS");
        if (githubActions != null && githubActions.equals("true")) {
            return "GitHub";
        } else {
            return "local";
        }
    }

    // ============================================
    // CORE CONFIGURATION
    // ============================================
    public static final String HOST = env.get("HOST", "cdn.contentstack.io");
    public static final String API_KEY = env.get("API_KEY", "");
    public static final String DELIVERY_TOKEN = env.get("DELIVERY_TOKEN", "");
    public static final String ENVIRONMENT = env.get("ENVIRONMENT", "development");
    public static final String MANAGEMENT_TOKEN = env.get("MANAGEMENT_TOKEN", "");
    public static final String PREVIEW_TOKEN = env.get("PREVIEW_TOKEN", "");
    public static final String LIVE_PREVIEW_HOST = env.get("LIVE_PREVIEW_HOST", "preview.contentstack.io");

    // ============================================
    // BACKWARD COMPATIBILITY (Existing Tests)
    // ============================================
    public static final String CONTENT_TYPE = env.get("contentType", "");
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

    // ============================================
    // ENTRY UIDs (New Test Data)
    // ============================================
    public static final String COMPLEX_ENTRY_UID = env.get("COMPLEX_ENTRY_UID", "");
    public static final String MEDIUM_ENTRY_UID = env.get("MEDIUM_ENTRY_UID", "");
    public static final String SIMPLE_ENTRY_UID = env.get("SIMPLE_ENTRY_UID", "");
    public static final String SELF_REF_ENTRY_UID = env.get("SELF_REF_ENTRY_UID", "");
    public static final String COMPLEX_BLOCKS_ENTRY_UID = env.get("COMPLEX_BLOCKS_ENTRY_UID", "");

    // ============================================
    // CONTENT TYPE UIDs (New Test Data)
    // ============================================
    public static final String COMPLEX_CONTENT_TYPE_UID = env.get("COMPLEX_CONTENT_TYPE_UID", "");
    public static final String MEDIUM_CONTENT_TYPE_UID = env.get("MEDIUM_CONTENT_TYPE_UID", "");
    public static final String SIMPLE_CONTENT_TYPE_UID = env.get("SIMPLE_CONTENT_TYPE_UID", "");
    public static final String SELF_REF_CONTENT_TYPE_UID = env.get("SELF_REF_CONTENT_TYPE_UID", "");
    public static final String COMPLEX_BLOCKS_CONTENT_TYPE_UID = env.get("COMPLEX_BLOCKS_CONTENT_TYPE_UID", "");

    // ============================================
    // ASSET UIDs
    // ============================================
    public static final String IMAGE_ASSET_UID = env.get("IMAGE_ASSET_UID", "");

    // ============================================
    // TAXONOMY TERMS
    // ============================================
    public static final String TAX_USA_STATE = env.get("TAX_USA_STATE", "");
    public static final String TAX_INDIA_STATE = env.get("TAX_INDIA_STATE", "");

    // ============================================
    // BRANCH
    // ============================================
    public static final String BRANCH_UID = env.get("BRANCH_UID", "");

    // ============================================
    // GLOBAL FIELDS
    // ============================================
    public static final String GLOBAL_FIELD_SIMPLE = env.get("GLOBAL_FIELD_SIMPLE", "");
    public static final String GLOBAL_FIELD_MEDIUM = env.get("GLOBAL_FIELD_MEDIUM", "");
    public static final String GLOBAL_FIELD_COMPLEX = env.get("GLOBAL_FIELD_COMPLEX", "");
    public static final String GLOBAL_FIELD_VIDEO = env.get("GLOBAL_FIELD_VIDEO", "");

    // ============================================
    // LOCALES
    // ============================================
    public static final String PRIMARY_LOCALE = env.get("PRIMARY_LOCALE", "");
    public static final String FALLBACK_LOCALE = env.get("FALLBACK_LOCALE", "");

    // ============================================
    // VALIDATION METHODS
    // ============================================
    
    /**
     * Check if complex entry configuration is available
     */
    public static boolean hasComplexEntry() {
        return COMPLEX_ENTRY_UID != null && !COMPLEX_ENTRY_UID.isEmpty();
    }

    /**
     * Check if taxonomy support is configured
     */
    public static boolean hasTaxonomySupport() {
        return TAX_USA_STATE != null && !TAX_USA_STATE.isEmpty()
                && TAX_INDIA_STATE != null && !TAX_INDIA_STATE.isEmpty();
    }

    /**
     * Check if variant support is configured
     */
    public static boolean hasVariantSupport() {
        return VARIANT_UID != null && !VARIANT_UID.isEmpty();
    }

    /**
     * Check if global field configuration is available
     */
    public static boolean hasGlobalFieldsConfigured() {
        return GLOBAL_FIELD_SIMPLE != null && GLOBAL_FIELD_COMPLEX != null;
    }

    /**
     * Check if locale fallback is configured
     */
    public static boolean hasLocaleFallback() {
        return FALLBACK_LOCALE != null && !FALLBACK_LOCALE.isEmpty();
    }

    /**
     * Get test data summary for logging
     */
    public static String getTestDataSummary() {
        return String.format(
            "Test Data Configuration:\n" +
            "  Complex Entry: %s (%s)\n" +
            "  Medium Entry: %s (%s)\n" +
            "  Simple Entry: %s (%s)\n" +
            "  Variant: %s\n" +
            "  Taxonomies: %s, %s\n" +
            "  Branch: %s",
            COMPLEX_ENTRY_UID, COMPLEX_CONTENT_TYPE_UID,
            MEDIUM_ENTRY_UID, MEDIUM_CONTENT_TYPE_UID,
            SIMPLE_ENTRY_UID, SIMPLE_CONTENT_TYPE_UID,
            VARIANT_UID,
            TAX_USA_STATE, TAX_INDIA_STATE,
            BRANCH_UID
        );
    }

    /**
     * Check if medium entry configuration is available
     */
    public static boolean hasMediumEntry() {
        return MEDIUM_ENTRY_UID != null && !MEDIUM_ENTRY_UID.isEmpty();
    }

    /**
     * Check if simple entry configuration is available
     */
    public static boolean hasSimpleEntry() {
        return SIMPLE_ENTRY_UID != null && !SIMPLE_ENTRY_UID.isEmpty();
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
