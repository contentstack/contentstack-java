package com.contentstack.sdk;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

/**
 * Comprehensive test cases for the ResponseType enum
 */
class TestResponseType {

    @Test
    void testResponseTypeValues() {
        ResponseType[] types = ResponseType.values();
        
        assertEquals(2, types.length);
        assertEquals(ResponseType.NETWORK, types[0]);
        assertEquals(ResponseType.UNKNOWN, types[1]);
    }

    @Test
    void testResponseTypeValueOf() {
        assertEquals(ResponseType.NETWORK, ResponseType.valueOf("NETWORK"));
        assertEquals(ResponseType.UNKNOWN, ResponseType.valueOf("UNKNOWN"));
    }

    @Test
    void testResponseTypeNetworkExists() {
        ResponseType network = ResponseType.NETWORK;
        assertNotNull(network);
        assertEquals("NETWORK", network.name());
    }

    @Test
    void testResponseTypeUnknownExists() {
        ResponseType unknown = ResponseType.UNKNOWN;
        assertNotNull(unknown);
        assertEquals("UNKNOWN", unknown.name());
    }

    @Test
    void testResponseTypeComparison() {
        ResponseType type1 = ResponseType.NETWORK;
        ResponseType type2 = ResponseType.NETWORK;
        ResponseType type3 = ResponseType.UNKNOWN;
        
        assertEquals(type1, type2);
        assertNotEquals(type1, type3);
    }

    @Test
    void testResponseTypeInSwitchStatement() {
        ResponseType type = ResponseType.NETWORK;
        String result;
        
        switch (type) {
            case NETWORK:
                result = "network";
                break;
            case UNKNOWN:
                result = "unknown";
                break;
            default:
                result = "other";
                break;
        }
        
        assertEquals("network", result);
    }

    @Test
    void testResponseTypeUnknownInSwitchStatement() {
        ResponseType type = ResponseType.UNKNOWN;
        String result;
        
        switch (type) {
            case NETWORK:
                result = "network";
                break;
            case UNKNOWN:
                result = "unknown";
                break;
            default:
                result = "other";
                break;
        }
        
        assertEquals("unknown", result);
    }

    @Test
    void testInvalidValueOfThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> {
            ResponseType.valueOf("INVALID");
        });
    }

    @Test
    void testEnumOrdinals() {
        assertEquals(0, ResponseType.NETWORK.ordinal());
        assertEquals(1, ResponseType.UNKNOWN.ordinal());
    }

    @Test
    void testEnumToString() {
        assertEquals("NETWORK", ResponseType.NETWORK.toString());
        assertEquals("UNKNOWN", ResponseType.UNKNOWN.toString());
    }
}

