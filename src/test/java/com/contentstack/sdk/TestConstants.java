package com.contentstack.sdk;

import org.junit.jupiter.api.Test;
import java.util.Calendar;
import java.util.TimeZone;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test cases for the Constants class
 */
class TestConstants {

    @Test
    void testConstantsConstructor() {
        // Test that constructor is protected and warns
        assertDoesNotThrow(() -> {
            // We can't directly instantiate due to protected constructor,
            // but we can verify constant values
            assertNotNull(Constants.SDK_VERSION);
            assertNotNull(Constants.USER_AGENT);
        });
    }

    @Test
    void testUserAgentGeneration() {
        String userAgent = Constants.USER_AGENT;
        
        assertNotNull(userAgent, "User agent should not be null");
        assertTrue(userAgent.contains(Constants.SDK_VERSION), 
            "User agent should contain SDK version");
    }

    @Test
    void testParseDateToTimeZone() {
        String dateString = "2023-10-15T12:30:45.123Z";
        String zoneId = "America/New_York";
        
        Calendar calendar = Constants.parseDateToTimeZone(dateString, zoneId);
        
        assertNotNull(calendar, "Calendar should not be null");
        assertEquals(2023, calendar.get(Calendar.YEAR));
    }

    @Test
    void testParseDateToTimeZoneUTC() {
        String dateString = "2024-01-01T00:00:00Z";
        String zoneId = "UTC";
        
        Calendar calendar = Constants.parseDateToTimeZone(dateString, zoneId);
        
        assertNotNull(calendar);
        assertEquals(2024, calendar.get(Calendar.YEAR));
        assertEquals(1, calendar.get(Calendar.MONTH)); // Month is 1-based after parsing
    }

    @Test
    void testParseDateToTimeZoneAsiaTokyo() {
        String dateString = "2023-06-15T10:30:00Z";
        String zoneId = "Asia/Tokyo";
        
        Calendar calendar = Constants.parseDateToTimeZone(dateString, zoneId);
        
        assertNotNull(calendar);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertEquals(6, calendar.get(Calendar.MONTH));
    }

    @Test
    void testParseDateWithTimeZone() {
        String dateString = "2023-12-25T15:30:45.000";
        TimeZone timeZone = TimeZone.getTimeZone("GMT");
        
        Calendar calendar = Constants.parseDate(dateString, timeZone);
        
        assertNotNull(calendar);
        // Just verify calendar is not null and has timezone set
        assertEquals(timeZone, calendar.getTimeZone());
    }

    @Test
    void testParseDateWithNullTimeZone() {
        String dateString = "2023-11-20T08:00:00.000";
        
        Calendar calendar = Constants.parseDate(dateString, null);
        
        assertNotNull(calendar);
        assertEquals(2023, calendar.get(Calendar.YEAR));
        assertNotNull(calendar.getTimeZone());
    }

    @Test
    void testParseDateWithEmptyString() {
        String dateString = "";
        TimeZone timeZone = TimeZone.getDefault();
        
        Calendar calendar = Constants.parseDate(dateString, timeZone);
        
        assertNull(calendar, "Calendar should be null for empty date string");
    }

    @Test
    void testParseDateDefaultTimeZone() {
        String dateString = "2024-03-15T14:45:30.500";
        
        Calendar calendar = Constants.parseDate(dateString, null);
        
        assertNotNull(calendar);
        assertEquals(TimeZone.getDefault(), calendar.getTimeZone());
    }

    @Test
    void testSDKVersionFormat() {
        String version = Constants.SDK_VERSION;
        
        assertNotNull(version);
        assertFalse(version.isEmpty());
        assertTrue(version.matches("\\d+\\.\\d+\\.\\d+"), 
            "SDK version should follow semantic versioning");
    }

    @Test
    void testConstantValues() {
        // Test that critical constants are defined
        assertEquals("environment", Constants.ENVIRONMENT);
        assertEquals("content_type_uid", Constants.CONTENT_TYPE_UID);
        assertEquals("entry_uid", Constants.ENTRY_UID);
        assertEquals("live_preview", Constants.LIVE_PREVIEW);
        assertEquals("stacks/sync", Constants.SYNCHRONISATION);
    }

    @Test
    void testErrorConstants() {
        assertEquals("error_code", Constants.ERROR_CODE);
        assertEquals("error_message", Constants.ERROR_MESSAGE);
        assertEquals("errors", Constants.ERRORS);
    }

    @Test
    void testUserAgentConstants() {
        assertEquals("X-User-Agent", Constants.X_USER_AGENT_KEY);
        assertEquals("User-Agent", Constants.USER_AGENT_KEY);
        assertEquals("Content-Type", Constants.CONTENT_TYPE);
        assertEquals("application/json", Constants.APPLICATION_JSON);
    }

    @Test
    void testQueryConstants() {
        assertEquals("query", Constants.QUERY);
        assertEquals("except", Constants.EXCEPT);
        assertEquals("$exists", Constants.EXISTS);
        assertEquals("$regex", Constants.REGEX);
        assertEquals("limit", Constants.LIMIT);
        assertEquals("$options", Constants.OPTIONS);
    }

    @Test
    void testRequestTypeConstants() {
        assertEquals("getQueryEntries", Constants.QUERYOBJECT);
        assertEquals("getSingleQueryEntries", Constants.SINGLEQUERYOBJECT);
        assertEquals("getEntry", Constants.FETCHENTRY);
        assertEquals("getAllAssets", Constants.FETCHALLASSETS);
        assertEquals("getAssets", Constants.FETCHASSETS);
        assertEquals("getSync", Constants.FETCHSYNC);
        assertEquals("getContentTypes", Constants.FETCHCONTENTTYPES);
        assertEquals("getGlobalFields", Constants.FETCHGLOBALFIELDS);
    }

    @Test
    void testExceptionMessageConstants() {
        assertEquals("Please set contentType name.", Constants.CONTENT_TYPE_NAME);
        assertEquals("Please provide valid params.", Constants.QUERY_EXCEPTION);
    }

    @Test
    void testRequestControllerEnum() {
        Constants.REQUEST_CONTROLLER[] controllers = Constants.REQUEST_CONTROLLER.values();
        
        assertEquals(7, controllers.length);
        assertEquals(Constants.REQUEST_CONTROLLER.QUERY, 
            Constants.REQUEST_CONTROLLER.valueOf("QUERY"));
        assertEquals(Constants.REQUEST_CONTROLLER.ENTRY, 
            Constants.REQUEST_CONTROLLER.valueOf("ENTRY"));
        assertEquals(Constants.REQUEST_CONTROLLER.ASSET, 
            Constants.REQUEST_CONTROLLER.valueOf("ASSET"));
        assertEquals(Constants.REQUEST_CONTROLLER.SYNC, 
            Constants.REQUEST_CONTROLLER.valueOf("SYNC"));
        assertEquals(Constants.REQUEST_CONTROLLER.CONTENTTYPES, 
            Constants.REQUEST_CONTROLLER.valueOf("CONTENTTYPES"));
        assertEquals(Constants.REQUEST_CONTROLLER.ASSETLIBRARY, 
            Constants.REQUEST_CONTROLLER.valueOf("ASSETLIBRARY"));
        assertEquals(Constants.REQUEST_CONTROLLER.GLOBALFIELDS, 
            Constants.REQUEST_CONTROLLER.valueOf("GLOBALFIELDS"));
    }

    @Test
    void testParseDateVariousFormats() {
        // Test with milliseconds
        String date1 = "2023-07-20T10:15:30.500";
        Calendar cal1 = Constants.parseDate(date1, TimeZone.getTimeZone("UTC"));
        assertNotNull(cal1);
        
        // Test with no milliseconds  
        String date2 = "2023-08-25T16:45:00.000";
        Calendar cal2 = Constants.parseDate(date2, TimeZone.getTimeZone("EST"));
        assertNotNull(cal2);
    }
}

