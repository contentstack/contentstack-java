package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.List;

public class TestSyncStack {
    private SyncStack syncStack;

    @BeforeEach
    void setUp() {
        syncStack = new SyncStack();
    }

    /**
     * ✅ Test: Valid JSON with correct structure
     */
    @Test
    void testSetJSON_WithValidData() {
        JSONObject validJson = new JSONObject()
                .put("items", new JSONArray()
                        .put(new JSONObject().put("title", "Article 1"))
                        .put(new JSONObject().put("title", "Article 2")))
                .put("skip", 5)
                .put("total_count", 100)
                .put("limit", 20)
                .put("pagination_token", "validToken123")
                .put("sync_token", "sync123");

        syncStack.setJSON(validJson);

        // Assertions
        assertEquals(5, syncStack.getSkip());
        assertEquals(100, syncStack.getCount());
        assertEquals(20, syncStack.getLimit());
        assertEquals("validToken123", syncStack.getPaginationToken());
        assertEquals("sync123", syncStack.getSyncToken());

        List<JSONObject> items = syncStack.getItems();
        assertNotNull(items);
        assertEquals(2, items.size());
        assertEquals("Article 1", items.get(0).optString("title"));
    }

    /**
     * ✅ Test: Missing `items` should not cause a crash
     */
    @Test
    void testSetJSON_MissingItems() {
        JSONObject jsonWithoutItems = new JSONObject()
                .put("skip", 5)
                .put("total_count", 50)
                .put("limit", 10);

        syncStack.setJSON(jsonWithoutItems);

        // Assertions
        assertEquals(5, syncStack.getSkip());
        assertEquals(50, syncStack.getCount());
        assertEquals(10, syncStack.getLimit());
        assertTrue(syncStack.getItems().isEmpty()); // Should default to empty list
    }

    /**
     * ✅ Test: Handling JSON Injection Attempt
     */
    @Test
    void testSetJSON_JSONInjection() {
        JSONObject maliciousJson = new JSONObject()
                .put("items", new JSONArray()
                        .put(new JSONObject().put("title", "<script>alert('Hacked');</script>")));

        syncStack.setJSON(maliciousJson);

        List<JSONObject> items = syncStack.getItems();
        assertNotNull(items);
        assertEquals(1, items.size());
        assertEquals("&lt;script&gt;alert('Hacked');&lt;/script&gt;", items.get(0).optString("title"));
    }

    /**
     * ✅ Should treat a lone JSONObject under "items" the same as a one‑element
     * array.
     */
    @Test
    void testSetJSON_handlesSingleItemObject() {
        JSONObject input = new JSONObject()
                .put("items", new JSONObject()
                        .put("title", "Single Entry")
                        .put("uid", "entry123")
                        .put("content_type", "blog"))
                .put("skip", 0)
                .put("total_count", 1)
                .put("limit", 10)
                .put("sync_token", "token123");

        syncStack.setJSON(input);
        List<JSONObject> items = syncStack.getItems();

        assertNotNull(items, "Items list should be initialised");
        assertEquals(1, items.size(), "Exactly one item expected");

        JSONObject item = items.get(0);
        assertEquals("Single Entry", item.optString("title"));
        assertEquals("entry123", item.optString("uid"));
        assertEquals("blog", item.optString("content_type"));

        assertEquals(0, syncStack.getSkip());
        assertEquals(1, syncStack.getCount());
        assertEquals(10, syncStack.getLimit());
        assertEquals("token123", syncStack.getSyncToken());
    }

    /**
     * ✅ Test: Invalid `items` field (should not crash)
     */
    @Test
    void testSetJSON_InvalidItemsType() {
        JSONObject invalidJson = new JSONObject()
                .put("items", "This is not a valid array")
                .put("skip", 10);

        assertDoesNotThrow(() -> syncStack.setJSON(invalidJson));
        assertTrue(syncStack.getItems().isEmpty());
    }

    /**
     * ✅ Test: Null `paginationToken` and `syncToken` are handled correctly
     */
    @Test
    void testSetJSON_NullTokens() {
        JSONObject jsonWithNullTokens = new JSONObject()
                .put("pagination_token", JSONObject.NULL)
                .put("sync_token", JSONObject.NULL);

        syncStack.setJSON(jsonWithNullTokens);

        assertNull(syncStack.getPaginationToken());
        assertNull(syncStack.getSyncToken());
    }

    /**
     * ✅ Test: Invalid characters in `paginationToken` should be rejected
     */
    @Test
    void testSetJSON_InvalidTokenCharacters() {
        JSONObject jsonWithInvalidTokens = new JSONObject()
                .put("pagination_token", "invalid!!@#")
                .put("sync_token", "<script>attack</script>");

        syncStack.setJSON(jsonWithInvalidTokens);

        assertNull(syncStack.getPaginationToken()); // Should be sanitized
        assertNull(syncStack.getSyncToken()); // Should be sanitized
    }

    /**
     * ✅ Test: Thread-Safety - Concurrent Modification of `syncItems`
     */
    @Test
    void testSetJSON_ThreadSafety() throws InterruptedException {
        JSONObject jsonWithItems = new JSONObject()
                .put("items", new JSONArray()
                        .put(new JSONObject().put("title", "Safe Entry")));

        Thread thread1 = new Thread(() -> syncStack.setJSON(jsonWithItems));
        Thread thread2 = new Thread(() -> syncStack.setJSON(jsonWithItems));

        thread1.start();
        thread2.start();
        thread1.join();
        thread2.join();

        assertFalse(syncStack.getItems().isEmpty()); // No race conditions
    }
}
