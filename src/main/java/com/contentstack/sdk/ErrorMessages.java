package com.contentstack.sdk;

/**
 * Centralized error messages for the Contentstack SDK.
 * This class contains all user-facing error messages to ensure consistency
 * and make maintenance easier.
 */
public final class ErrorMessages {

    // Prevent instantiation
    private ErrorMessages() {
        throw new UnsupportedOperationException("This is a utility class and cannot be instantiated");
    }

    // ========== AUTHENTICATION & ACCESS ERRORS ==========
    
    public static final String MISSING_API_KEY = "Missing API key. Provide a valid key from your Contentstack stack settings and try again.";
    public static final String MISSING_DELIVERY_TOKEN = "Missing delivery token. Provide a valid token from your Contentstack stack settings and try again.";
    public static final String MISSING_ENVIRONMENT = "Missing environment. Provide a valid environment name and try again.";
    public static final String MISSING_REQUEST_HEADERS = "Missing request headers. Provide api_key, access_token, and environment, then try again.";
    
    // ========== DIRECT INSTANTIATION ERRORS ==========
    
    public static final String DIRECT_INSTANTIATION_STACK = "Direct instantiation of Stack is not allowed. Use Contentstack.stack() to create an instance.";
    public static final String DIRECT_INSTANTIATION_CONTENTSTACK = "Direct instantiation of Stack is not allowed. Use Contentstack.stack() to create an instance.";
    public static final String DIRECT_INSTANTIATION_CONTENT_TYPE = "Direct instantiation of ContentType is not allowed. Use Stack.contentType(uid) to create an instance.";
    public static final String DIRECT_INSTANTIATION_ENTRY = "Direct instantiation of Entry is not allowed. Use ContentType.entry(uid) to create an instance.";
    
    // ========== REQUIRED FIELD ERRORS ==========
    
    public static final String CONTENT_TYPE_UID_REQUIRED = "Content type UID is required. Provide a valid UID and try again.";
    public static final String ENTRY_UID_REQUIRED = "Missing entry UID. Provide a valid UID and try again.";
    public static final String GLOBAL_FIELD_UID_REQUIRED = "Missing global field UID. Provide a valid UID and try again.";
    
    // ========== DATA VALIDATION ERRORS ==========
    
    public static final String INVALID_PARAMETER_KEY = "Invalid parameter key. Use only alphanumeric characters, underscores, and dots.";
    public static final String INVALID_PARAMETER_VALUE = "Invalid parameter value. Remove unsupported characters and try again.";
    public static final String INVALID_QUERY_URL = "Invalid query URL. Use a valid URL and try again.";
    public static final String INVALID_DATE_FORMAT = "Invalid date format for field. Provide the date in ISO format and try again.";
    
    // ========== DATA TYPE ERRORS ==========
    
    public static final String INVALID_ASSETS_TYPE = "Invalid type for 'assets' key. Provide assets as a List or ArrayList and try again.";
    public static final String INVALID_OBJECT_TYPE_ASSET_MODEL = "Invalid object type. Use an AssetModel object and try again.";
    public static final String INVALID_CONTENT_TYPE_DATA = "Invalid content type data. Provide a LinkedHashMap structure and try again.";
    public static final String INVALID_CONTENT_TYPES_LIST = "Invalid type in content types list. Use a LinkedHashMap and try again.";
    public static final String INVALID_GLOBAL_FIELD_DATA = "Invalid global field data. Provide a LinkedHashMap structure and try again.";
    public static final String INVALID_GLOBAL_FIELDS_LIST = "Invalid type in global fields list. Use a LinkedHashMap and try again.";
    
    // ========== MISSING DATA ERRORS ==========
    
    public static final String MISSING_ASSETS_LIST = "Missing assets list. Provide a valid list of assets and try again.";
    public static final String MISSING_JSON_OBJECT_SYNC = "Missing JSON object for sync operation. Provide a valid JSON object with sync parameters and try again.";
    
    // ========== NETWORK & CONNECTION ERRORS ==========
    
    public static final String URL_PARAMETER_ENCODING_FAILED = "URL parameter encoding failed. Provide a valid key and value, then try again.";
    public static final String LIVE_PREVIEW_URL_FAILED = "Failed to execute the Live Preview URL. Check your connection and try again.";
    public static final String TAXONOMY_QUERY_FAILED = "Failed to execute taxonomy query. Check your network connection and verify taxonomy parameters.";
    public static final String INVALID_JSON_RESPONSE = "Invalid JSON response. Check the server response format and try again.";
    
    // ========== CONFIGURATION ERRORS ==========
    
    public static final String MISSING_PREVIEW_TOKEN = "Missing preview token for rest-preview.contentstack.com. Set the preview token in your configuration to use Live Preview.";
    public static final String LIVE_PREVIEW_NOT_ENABLED = "Live Preview is not enabled in the configuration. Enable it and try again.";
    public static final String EMBEDDED_ITEMS_NOT_INCLUDED = "Embedded items are not included in the entry. Call includeEmbeddedItems() and try again.";
    
    // ========== OPERATION ERRORS ==========
    
    public static final String ENTRY_FETCH_FAILED = "Entry fetch operation failed due to missing UID. Provide a valid UID and try again.";
    public static final String QUERY_EXECUTION_FAILED = "Query execution failed. Check the query and try again.";
    public static final String ENTRIES_PROCESSING_FAILED = "Failed to process entries data. Check the entries format and try again.";
    public static final String GROUP_DATE_PARSING_FAILED = "Failed to parse date from group field. Provide a valid date format and try again.";
    public static final String QUERY_RESULT_PROCESSING_FAILED = "Failed to process query result data. Check the response format and try again.";
}
