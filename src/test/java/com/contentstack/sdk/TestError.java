package com.contentstack.sdk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test cases for the Error class
 */
class TestError {

    @Test
    void testDefaultConstructor() {
        Error error = new Error();
        
        assertNull(error.getErrorMessage(), "Error message should be null");
        assertEquals(0, error.getErrorCode(), "Error code should be 0");
        assertNull(error.getErrorDetail(), "Error details should be null");
    }

    @Test
    void testParameterizedConstructor() {
        String expectedMessage = "Test error message";
        int expectedCode = 404;
        String expectedDetails = "Resource not found";
        
        Error error = new Error(expectedMessage, expectedCode, expectedDetails);
        
        assertEquals(expectedMessage, error.getErrorMessage());
        assertEquals(expectedCode, error.getErrorCode());
        assertEquals(expectedDetails, error.getErrorDetail());
    }

    @Test
    void testSetErrorMessage() {
        Error error = new Error();
        String message = "Network error occurred";
        
        error.setErrorMessage(message);
        
        assertEquals(message, error.getErrorMessage());
    }

    @Test
    void testSetErrorCode() {
        Error error = new Error();
        int code = 500;
        
        error.setErrorCode(code);
        
        assertEquals(code, error.getErrorCode());
    }

    @Test
    void testSetErrorDetail() {
        Error error = new Error();
        String details = "Internal server error details";
        
        error.setErrorDetail(details);
        
        assertEquals(details, error.getErrorDetail());
    }

    @Test
    void testMultipleSettersChaining() {
        Error error = new Error();
        
        error.setErrorMessage("Unauthorized");
        error.setErrorCode(401);
        error.setErrorDetail("Invalid credentials provided");
        
        assertEquals("Unauthorized", error.getErrorMessage());
        assertEquals(401, error.getErrorCode());
        assertEquals("Invalid credentials provided", error.getErrorDetail());
    }

    @Test
    void testErrorWithNullValues() {
        Error error = new Error(null, 0, null);
        
        assertNull(error.getErrorMessage());
        assertEquals(0, error.getErrorCode());
        assertNull(error.getErrorDetail());
    }

    @Test
    void testErrorWithEmptyStrings() {
        Error error = new Error("", -1, "");
        
        assertEquals("", error.getErrorMessage());
        assertEquals(-1, error.getErrorCode());
        assertEquals("", error.getErrorDetail());
    }

    @Test
    void testErrorModification() {
        Error error = new Error("Initial message", 100, "Initial details");
        
        error.setErrorMessage("Modified message");
        error.setErrorCode(200);
        error.setErrorDetail("Modified details");
        
        assertEquals("Modified message", error.getErrorMessage());
        assertEquals(200, error.getErrorCode());
        assertEquals("Modified details", error.getErrorDetail());
    }

    @Test
    void testCommonHTTPErrorCodes() {
        // Test various common HTTP error codes
        Error error400 = new Error("Bad Request", 400, "Invalid syntax");
        assertEquals(400, error400.getErrorCode());
        
        Error error401 = new Error("Unauthorized", 401, "Authentication required");
        assertEquals(401, error401.getErrorCode());
        
        Error error403 = new Error("Forbidden", 403, "Access denied");
        assertEquals(403, error403.getErrorCode());
        
        Error error404 = new Error("Not Found", 404, "Resource not found");
        assertEquals(404, error404.getErrorCode());
        
        Error error500 = new Error("Internal Server Error", 500, "Server error");
        assertEquals(500, error500.getErrorCode());
    }
}

