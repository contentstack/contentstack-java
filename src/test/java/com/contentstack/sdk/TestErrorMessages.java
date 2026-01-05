package com.contentstack.sdk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive unit tests for the ErrorMessages utility class.
 * Tests all error message constants and ensures the class cannot be instantiated.
 */
public class TestErrorMessages {

    @Test
    void testCannotInstantiateErrorMessages() {
        Exception exception = assertThrows(Exception.class, () -> {
            // Use reflection to access private constructor
            java.lang.reflect.Constructor<ErrorMessages> constructor = 
                ErrorMessages.class.getDeclaredConstructor();
            constructor.setAccessible(true);
            try {
                constructor.newInstance();
            } catch (java.lang.reflect.InvocationTargetException e) {
                // Unwrap and rethrow the actual exception
                throw e.getCause();
            }
        });
        assertTrue(exception instanceof UnsupportedOperationException);
        assertTrue(exception.getMessage().contains("utility class"));
    }

    // ========== AUTHENTICATION & ACCESS ERRORS TESTS ==========

    @Test
    void testMissingApiKeyMessage() {
        assertNotNull(ErrorMessages.MISSING_API_KEY);
        assertFalse(ErrorMessages.MISSING_API_KEY.isEmpty());
        assertTrue(ErrorMessages.MISSING_API_KEY.contains("API key"));
    }

    @Test
    void testMissingDeliveryTokenMessage() {
        assertNotNull(ErrorMessages.MISSING_DELIVERY_TOKEN);
        assertFalse(ErrorMessages.MISSING_DELIVERY_TOKEN.isEmpty());
        assertTrue(ErrorMessages.MISSING_DELIVERY_TOKEN.contains("delivery token"));
    }

    @Test
    void testMissingEnvironmentMessage() {
        assertNotNull(ErrorMessages.MISSING_ENVIRONMENT);
        assertFalse(ErrorMessages.MISSING_ENVIRONMENT.isEmpty());
        assertTrue(ErrorMessages.MISSING_ENVIRONMENT.contains("environment"));
    }

    @Test
    void testMissingRequestHeadersMessage() {
        assertNotNull(ErrorMessages.MISSING_REQUEST_HEADERS);
        assertFalse(ErrorMessages.MISSING_REQUEST_HEADERS.isEmpty());
        assertTrue(ErrorMessages.MISSING_REQUEST_HEADERS.contains("headers"));
    }

    // ========== DIRECT INSTANTIATION ERRORS TESTS ==========

    @Test
    void testDirectInstantiationStackMessage() {
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_STACK);
        assertFalse(ErrorMessages.DIRECT_INSTANTIATION_STACK.isEmpty());
        assertTrue(ErrorMessages.DIRECT_INSTANTIATION_STACK.contains("Stack"));
        assertTrue(ErrorMessages.DIRECT_INSTANTIATION_STACK.contains("Contentstack.stack()"));
    }

    @Test
    void testDirectInstantiationContentstackMessage() {
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_CONTENTSTACK);
        assertFalse(ErrorMessages.DIRECT_INSTANTIATION_CONTENTSTACK.isEmpty());
    }

    @Test
    void testDirectInstantiationContentTypeMessage() {
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_CONTENT_TYPE);
        assertFalse(ErrorMessages.DIRECT_INSTANTIATION_CONTENT_TYPE.isEmpty());
        assertTrue(ErrorMessages.DIRECT_INSTANTIATION_CONTENT_TYPE.contains("ContentType"));
    }

    @Test
    void testDirectInstantiationEntryMessage() {
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_ENTRY);
        assertFalse(ErrorMessages.DIRECT_INSTANTIATION_ENTRY.isEmpty());
        assertTrue(ErrorMessages.DIRECT_INSTANTIATION_ENTRY.contains("Entry"));
    }

    // ========== REQUIRED FIELD ERRORS TESTS ==========

    @Test
    void testContentTypeUidRequiredMessage() {
        assertNotNull(ErrorMessages.CONTENT_TYPE_UID_REQUIRED);
        assertFalse(ErrorMessages.CONTENT_TYPE_UID_REQUIRED.isEmpty());
        assertTrue(ErrorMessages.CONTENT_TYPE_UID_REQUIRED.contains("UID"));
    }

    @Test
    void testEntryUidRequiredMessage() {
        assertNotNull(ErrorMessages.ENTRY_UID_REQUIRED);
        assertFalse(ErrorMessages.ENTRY_UID_REQUIRED.isEmpty());
        assertTrue(ErrorMessages.ENTRY_UID_REQUIRED.contains("UID"));
    }

    @Test
    void testGlobalFieldUidRequiredMessage() {
        assertNotNull(ErrorMessages.GLOBAL_FIELD_UID_REQUIRED);
        assertFalse(ErrorMessages.GLOBAL_FIELD_UID_REQUIRED.isEmpty());
        assertTrue(ErrorMessages.GLOBAL_FIELD_UID_REQUIRED.contains("global field"));
    }

    // ========== DATA VALIDATION ERRORS TESTS ==========

    @Test
    void testInvalidParameterKeyMessage() {
        assertNotNull(ErrorMessages.INVALID_PARAMETER_KEY);
        assertFalse(ErrorMessages.INVALID_PARAMETER_KEY.isEmpty());
        assertTrue(ErrorMessages.INVALID_PARAMETER_KEY.contains("parameter key"));
    }

    @Test
    void testInvalidParameterValueMessage() {
        assertNotNull(ErrorMessages.INVALID_PARAMETER_VALUE);
        assertFalse(ErrorMessages.INVALID_PARAMETER_VALUE.isEmpty());
        assertTrue(ErrorMessages.INVALID_PARAMETER_VALUE.contains("parameter value"));
    }

    @Test
    void testInvalidQueryUrlMessage() {
        assertNotNull(ErrorMessages.INVALID_QUERY_URL);
        assertFalse(ErrorMessages.INVALID_QUERY_URL.isEmpty());
        assertTrue(ErrorMessages.INVALID_QUERY_URL.contains("URL"));
    }

    @Test
    void testInvalidDateFormatMessage() {
        assertNotNull(ErrorMessages.INVALID_DATE_FORMAT);
        assertFalse(ErrorMessages.INVALID_DATE_FORMAT.isEmpty());
        assertTrue(ErrorMessages.INVALID_DATE_FORMAT.contains("date format"));
    }

    // ========== DATA TYPE ERRORS TESTS ==========

    @Test
    void testInvalidAssetsTypeMessage() {
        assertNotNull(ErrorMessages.INVALID_ASSETS_TYPE);
        assertFalse(ErrorMessages.INVALID_ASSETS_TYPE.isEmpty());
        assertTrue(ErrorMessages.INVALID_ASSETS_TYPE.contains("assets"));
    }

    @Test
    void testInvalidObjectTypeAssetModelMessage() {
        assertNotNull(ErrorMessages.INVALID_OBJECT_TYPE_ASSET_MODEL);
        assertFalse(ErrorMessages.INVALID_OBJECT_TYPE_ASSET_MODEL.isEmpty());
        assertTrue(ErrorMessages.INVALID_OBJECT_TYPE_ASSET_MODEL.contains("AssetModel"));
    }

    @Test
    void testInvalidContentTypeDataMessage() {
        assertNotNull(ErrorMessages.INVALID_CONTENT_TYPE_DATA);
        assertFalse(ErrorMessages.INVALID_CONTENT_TYPE_DATA.isEmpty());
        assertTrue(ErrorMessages.INVALID_CONTENT_TYPE_DATA.contains("content type"));
    }

    @Test
    void testInvalidContentTypesListMessage() {
        assertNotNull(ErrorMessages.INVALID_CONTENT_TYPES_LIST);
        assertFalse(ErrorMessages.INVALID_CONTENT_TYPES_LIST.isEmpty());
    }

    @Test
    void testInvalidGlobalFieldDataMessage() {
        assertNotNull(ErrorMessages.INVALID_GLOBAL_FIELD_DATA);
        assertFalse(ErrorMessages.INVALID_GLOBAL_FIELD_DATA.isEmpty());
        assertTrue(ErrorMessages.INVALID_GLOBAL_FIELD_DATA.contains("global field"));
    }

    @Test
    void testInvalidGlobalFieldsListMessage() {
        assertNotNull(ErrorMessages.INVALID_GLOBAL_FIELDS_LIST);
        assertFalse(ErrorMessages.INVALID_GLOBAL_FIELDS_LIST.isEmpty());
    }

    // ========== MISSING DATA ERRORS TESTS ==========

    @Test
    void testMissingAssetsListMessage() {
        assertNotNull(ErrorMessages.MISSING_ASSETS_LIST);
        assertFalse(ErrorMessages.MISSING_ASSETS_LIST.isEmpty());
        assertTrue(ErrorMessages.MISSING_ASSETS_LIST.contains("assets"));
    }

    @Test
    void testMissingJsonObjectSyncMessage() {
        assertNotNull(ErrorMessages.MISSING_JSON_OBJECT_SYNC);
        assertFalse(ErrorMessages.MISSING_JSON_OBJECT_SYNC.isEmpty());
        assertTrue(ErrorMessages.MISSING_JSON_OBJECT_SYNC.contains("sync"));
    }

    // ========== NETWORK & CONNECTION ERRORS TESTS ==========

    @Test
    void testUrlParameterEncodingFailedMessage() {
        assertNotNull(ErrorMessages.URL_PARAMETER_ENCODING_FAILED);
        assertFalse(ErrorMessages.URL_PARAMETER_ENCODING_FAILED.isEmpty());
        assertTrue(ErrorMessages.URL_PARAMETER_ENCODING_FAILED.contains("encoding"));
    }

    @Test
    void testLivePreviewUrlFailedMessage() {
        assertNotNull(ErrorMessages.LIVE_PREVIEW_URL_FAILED);
        assertFalse(ErrorMessages.LIVE_PREVIEW_URL_FAILED.isEmpty());
        assertTrue(ErrorMessages.LIVE_PREVIEW_URL_FAILED.contains("Live Preview"));
    }

    @Test
    void testTaxonomyQueryFailedMessage() {
        assertNotNull(ErrorMessages.TAXONOMY_QUERY_FAILED);
        assertFalse(ErrorMessages.TAXONOMY_QUERY_FAILED.isEmpty());
        assertTrue(ErrorMessages.TAXONOMY_QUERY_FAILED.contains("taxonomy"));
    }

    @Test
    void testInvalidJsonResponseMessage() {
        assertNotNull(ErrorMessages.INVALID_JSON_RESPONSE);
        assertFalse(ErrorMessages.INVALID_JSON_RESPONSE.isEmpty());
        assertTrue(ErrorMessages.INVALID_JSON_RESPONSE.contains("JSON"));
    }

    // ========== CONFIGURATION ERRORS TESTS ==========

    @Test
    void testMissingPreviewTokenMessage() {
        assertNotNull(ErrorMessages.MISSING_PREVIEW_TOKEN);
        assertFalse(ErrorMessages.MISSING_PREVIEW_TOKEN.isEmpty());
        assertTrue(ErrorMessages.MISSING_PREVIEW_TOKEN.contains("preview token"));
    }

    @Test
    void testLivePreviewNotEnabledMessage() {
        assertNotNull(ErrorMessages.LIVE_PREVIEW_NOT_ENABLED);
        assertFalse(ErrorMessages.LIVE_PREVIEW_NOT_ENABLED.isEmpty());
        assertTrue(ErrorMessages.LIVE_PREVIEW_NOT_ENABLED.contains("Live Preview"));
    }

    @Test
    void testEmbeddedItemsNotIncludedMessage() {
        assertNotNull(ErrorMessages.EMBEDDED_ITEMS_NOT_INCLUDED);
        assertFalse(ErrorMessages.EMBEDDED_ITEMS_NOT_INCLUDED.isEmpty());
        assertTrue(ErrorMessages.EMBEDDED_ITEMS_NOT_INCLUDED.contains("Embedded items"));
    }

    // ========== OPERATION ERRORS TESTS ==========

    @Test
    void testEntryFetchFailedMessage() {
        assertNotNull(ErrorMessages.ENTRY_FETCH_FAILED);
        assertFalse(ErrorMessages.ENTRY_FETCH_FAILED.isEmpty());
        assertTrue(ErrorMessages.ENTRY_FETCH_FAILED.contains("Entry fetch"));
    }

    @Test
    void testQueryExecutionFailedMessage() {
        assertNotNull(ErrorMessages.QUERY_EXECUTION_FAILED);
        assertFalse(ErrorMessages.QUERY_EXECUTION_FAILED.isEmpty());
        assertTrue(ErrorMessages.QUERY_EXECUTION_FAILED.contains("Query"));
    }

    @Test
    void testEntriesProcessingFailedMessage() {
        assertNotNull(ErrorMessages.ENTRIES_PROCESSING_FAILED);
        assertFalse(ErrorMessages.ENTRIES_PROCESSING_FAILED.isEmpty());
        assertTrue(ErrorMessages.ENTRIES_PROCESSING_FAILED.contains("entries"));
    }

    @Test
    void testGroupDateParsingFailedMessage() {
        assertNotNull(ErrorMessages.GROUP_DATE_PARSING_FAILED);
        assertFalse(ErrorMessages.GROUP_DATE_PARSING_FAILED.isEmpty());
        assertTrue(ErrorMessages.GROUP_DATE_PARSING_FAILED.contains("date"));
    }

    @Test
    void testQueryResultProcessingFailedMessage() {
        assertNotNull(ErrorMessages.QUERY_RESULT_PROCESSING_FAILED);
        assertFalse(ErrorMessages.QUERY_RESULT_PROCESSING_FAILED.isEmpty());
        assertTrue(ErrorMessages.QUERY_RESULT_PROCESSING_FAILED.contains("query result"));
    }

    // ========== COMPREHENSIVE ERROR MESSAGE FORMAT TESTS ==========

    @Test
    void testAllErrorMessagesAreNonNull() {
        assertNotNull(ErrorMessages.MISSING_API_KEY);
        assertNotNull(ErrorMessages.MISSING_DELIVERY_TOKEN);
        assertNotNull(ErrorMessages.MISSING_ENVIRONMENT);
        assertNotNull(ErrorMessages.MISSING_REQUEST_HEADERS);
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_STACK);
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_CONTENTSTACK);
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_CONTENT_TYPE);
        assertNotNull(ErrorMessages.DIRECT_INSTANTIATION_ENTRY);
        assertNotNull(ErrorMessages.CONTENT_TYPE_UID_REQUIRED);
        assertNotNull(ErrorMessages.ENTRY_UID_REQUIRED);
        assertNotNull(ErrorMessages.GLOBAL_FIELD_UID_REQUIRED);
    }

    @Test
    void testAllErrorMessagesHaveMinimumLength() {
        assertTrue(ErrorMessages.MISSING_API_KEY.length() > 20);
        assertTrue(ErrorMessages.MISSING_DELIVERY_TOKEN.length() > 20);
        assertTrue(ErrorMessages.DIRECT_INSTANTIATION_STACK.length() > 20);
        assertTrue(ErrorMessages.CONTENT_TYPE_UID_REQUIRED.length() > 20);
    }
}

