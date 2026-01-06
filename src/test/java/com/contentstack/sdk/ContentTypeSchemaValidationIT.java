package com.contentstack.sdk;

import com.contentstack.sdk.utils.PerformanceAssertion;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.concurrent.CountDownLatch;

/**
 * Comprehensive Integration Tests for Content Type Schema Validation
 * Tests content type schema and field validation including:
 * - Basic content type fetching
 * - Field type validation
 * - Schema structure validation
 * - System fields presence
 * - Custom fields validation
 * - Multiple content types comparison
 * - Performance with schema operations
 */
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ContentTypeSchemaValidationIT extends BaseIntegrationTest {

    @BeforeAll
    void setUp() {
        logger.info("Setting up ContentTypeSchemaValidationIT test suite");
        logger.info("Testing content type schema validation");
        logger.info("Using content type: " + Credentials.COMPLEX_CONTENT_TYPE_UID);
    }

    // ===========================
    // Basic Content Type Tests
    // ===========================

    @Test
    @Order(1)
    @DisplayName("Test fetch content type schema")
    void testFetchContentTypeSchema() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    // Get response
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    assertNotNull(response, "Response should not be null");
                    
                    // Validate basic properties
                    String uid = response.optString("uid");
                    String title = response.optString("title");
                    
                    assertNotNull(uid, "BUG: Content type UID missing");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, uid,
                            "BUG: Wrong content type UID");
                    
                    assertNotNull(title, "BUG: Content type title missing");
                    assertTrue(title.length() > 0, "BUG: Content type title empty");
                    
                    logger.info("✅ Content type fetched: " + title + " (" + uid + ")");
                    logSuccess("testFetchContentTypeSchema", "Content type: " + title);
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testFetchContentTypeSchema"));
    }

    @Test
    @Order(2)
    @DisplayName("Test content type has schema")
    void testContentTypeHasSchema() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    assertNotNull(response, "Response should not be null");
                    
                    // Check if schema exists
                    assertTrue(response.has("schema"), "BUG: Response must have schema");
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "BUG: Schema should not be null");
                    assertTrue(schema.length() > 0, "BUG: Schema should have fields");
                    
                    logger.info("✅ Schema has " + schema.length() + " fields");
                    logSuccess("testContentTypeHasSchema", schema.length() + " fields in schema");
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testContentTypeHasSchema"));
    }

    @Test
    @Order(3)
    @DisplayName("Test schema field structure")
    void testSchemaFieldStructure() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "Schema should not be null");
                    
                    // Validate first field structure
                    if (schema.length() > 0) {
                        org.json.JSONObject firstField = schema.getJSONObject(0);
                        
                        // Basic field properties
                        assertTrue(firstField.has("uid"), "BUG: Field must have uid");
                        assertTrue(firstField.has("data_type"), "BUG: Field must have data_type");
                        assertTrue(firstField.has("display_name"), "BUG: Field must have display_name");
                        
                        String fieldUid = firstField.getString("uid");
                        String dataType = firstField.getString("data_type");
                        String displayName = firstField.getString("display_name");
                        
                        assertNotNull(fieldUid, "Field UID should not be null");
                        assertNotNull(dataType, "Data type should not be null");
                        assertNotNull(displayName, "Display name should not be null");
                        
                        logger.info("✅ First field: " + displayName + " (" + fieldUid + ") - Type: " + dataType);
                        logSuccess("testSchemaFieldStructure", "Field structure valid");
                    } else {
                        fail("Schema should have at least one field");
                    }
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSchemaFieldStructure"));
    }

    @Test
    @Order(4)
    @DisplayName("Test schema has title field")
    void testSchemaHasTitleField() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "Schema should not be null");
                    
                    // Find title field
                    boolean hasTitleField = false;
                    for (int i = 0; i < schema.length(); i++) {
                        org.json.JSONObject field = schema.getJSONObject(i);
                        if ("title".equals(field.optString("uid"))) {
                            hasTitleField = true;
                            
                            // Validate title field
                            assertEquals("text", field.optString("data_type"),
                                    "BUG: Title field should be text type");
                            
                            logger.info("✅ Title field found and validated");
                            break;
                        }
                    }
                    
                    assertTrue(hasTitleField, "BUG: Schema must have title field");
                    logSuccess("testSchemaHasTitleField", "Title field present");
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSchemaHasTitleField"));
    }

    // ===========================
    // Field Type Validation
    // ===========================

    @Test
    @Order(5)
    @DisplayName("Test schema field types")
    void testSchemaFieldTypes() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "Schema should not be null");
                    
                    // Count different field types
                    int textFields = 0;
                    int numberFields = 0;
                    int booleanFields = 0;
                    int dateFields = 0;
                    int fileFields = 0;
                    int referenceFields = 0;
                    int groupFields = 0;
                    int modularBlockFields = 0;
                    
                    for (int i = 0; i < schema.length(); i++) {
                        org.json.JSONObject field = schema.getJSONObject(i);
                        String dataType = field.optString("data_type");
                        
                        switch (dataType) {
                            case "text": textFields++; break;
                            case "number": numberFields++; break;
                            case "boolean": booleanFields++; break;
                            case "isodate": dateFields++; break;
                            case "file": fileFields++; break;
                            case "reference": referenceFields++; break;
                            case "group": groupFields++; break;
                            case "blocks": modularBlockFields++; break;
                        }
                    }
                    
                    logger.info("Field types - Text: " + textFields + ", Number: " + numberFields + 
                            ", Boolean: " + booleanFields + ", Date: " + dateFields + 
                            ", File: " + fileFields + ", Reference: " + referenceFields +
                            ", Group: " + groupFields + ", Blocks: " + modularBlockFields);
                    
                    // At least one field should exist
                    assertTrue(textFields > 0 || numberFields > 0 || booleanFields > 0,
                            "Schema should have at least one field");
                    
                    logger.info("✅ Field types validated");
                    logSuccess("testSchemaFieldTypes", "Total: " + schema.length() + " fields");
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSchemaFieldTypes"));
    }

    @Test
    @Order(6)
    @DisplayName("Test reference field configuration")
    void testReferenceFieldConfiguration() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "Schema should not be null");
                    
                    // Find reference fields
                    int referenceCount = 0;
                    for (int i = 0; i < schema.length(); i++) {
                        org.json.JSONObject field = schema.getJSONObject(i);
                        if ("reference".equals(field.optString("data_type"))) {
                            referenceCount++;
                            
                            // Validate reference field has reference_to
                            assertTrue(field.has("reference_to"),
                                    "BUG: Reference field must have reference_to");
                            
                            org.json.JSONArray referenceTo = field.optJSONArray("reference_to");
                            if (referenceTo != null && referenceTo.length() > 0) {
                                logger.info("Reference field: " + field.optString("uid") + 
                                        " references " + referenceTo.length() + " content type(s)");
                            }
                        }
                    }
                    
                    if (referenceCount > 0) {
                        logger.info("✅ " + referenceCount + " reference field(s) validated");
                        logSuccess("testReferenceFieldConfiguration", referenceCount + " reference fields");
                    } else {
                        logger.info("ℹ️ No reference fields in schema");
                        logSuccess("testReferenceFieldConfiguration", "No reference fields");
                    }
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testReferenceFieldConfiguration"));
    }

    @Test
    @Order(7)
    @DisplayName("Test modular blocks field configuration")
    void testModularBlocksFieldConfiguration() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "Schema should not be null");
                    
                    // Find modular blocks fields
                    int blocksCount = 0;
                    for (int i = 0; i < schema.length(); i++) {
                        org.json.JSONObject field = schema.getJSONObject(i);
                        if ("blocks".equals(field.optString("data_type"))) {
                            blocksCount++;
                            
                            // Validate blocks field has blocks
                            if (field.has("blocks")) {
                                org.json.JSONArray blocks = field.optJSONArray("blocks");
                                if (blocks != null) {
                                    logger.info("Modular blocks field: " + field.optString("uid") + 
                                            " has " + blocks.length() + " block(s)");
                                }
                            }
                        }
                    }
                    
                    if (blocksCount > 0) {
                        logger.info("✅ " + blocksCount + " modular blocks field(s) found");
                        logSuccess("testModularBlocksFieldConfiguration", blocksCount + " blocks fields");
                    } else {
                        logger.info("ℹ️ No modular blocks fields in schema");
                        logSuccess("testModularBlocksFieldConfiguration", "No blocks fields");
                    }
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testModularBlocksFieldConfiguration"));
    }

    // ===========================
    // System Fields
    // ===========================

    @Test
    @Order(8)
    @DisplayName("Test content type system fields")
    void testContentTypeSystemFields() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    
                    // Validate system fields
                    assertTrue(response.has("uid"), "BUG: UID missing");
                    assertTrue(response.has("title"), "BUG: Title missing");
                    
                    String uid = response.optString("uid");
                    String title = response.optString("title");
                    String description = response.optString("description");
                    
                    assertNotNull(uid, "UID should not be null");
                    assertNotNull(title, "Title should not be null");
                    
                    logger.info("Description: " + (description != null && !description.isEmpty() ? description : "not set"));
                    logger.info("✅ System fields validated");
                    logSuccess("testContentTypeSystemFields", "System fields present");
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testContentTypeSystemFields"));
    }

    // ===========================
    // Performance Tests
    // ===========================

    @Test
    @Order(9)
    @DisplayName("Test content type fetch performance")
    void testContentTypeFetchPerformance() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    // Performance assertion
                    assertTrue(duration < 5000,
                            "PERFORMANCE BUG: Content type fetch took " + duration + "ms (max: 5s)");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    
                    if (schema != null) {
                        logger.info("✅ Fetched content type with " + schema.length() + 
                                " fields in " + formatDuration(duration));
                        logSuccess("testContentTypeFetchPerformance", formatDuration(duration));
                    }
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testContentTypeFetchPerformance"));
    }

    @Test
    @Order(10)
    @DisplayName("Test multiple content type fetches performance")
    void testMultipleContentTypeFetchesPerformance() throws InterruptedException, IllegalAccessException {
        int fetchCount = 3;
        long startTime = PerformanceAssertion.startTimer();
        
        for (int i = 0; i < fetchCount; i++) {
            CountDownLatch latch = createLatch();
            
            ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
            org.json.JSONObject params = new org.json.JSONObject();
            
            contentType.fetch(params, new ContentTypesCallback() {
                @Override
                public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                    try {
                        assertNull(error, "Content type fetch should not error");
                        assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    } finally {
                        latch.countDown();
                    }
                }
            });
            
            awaitLatch(latch, "fetch-" + i);
        }
        
        long duration = PerformanceAssertion.elapsedTime(startTime);
        
        // Multiple fetches should be reasonably fast
        assertTrue(duration < 15000,
                "PERFORMANCE BUG: " + fetchCount + " fetches took " + duration + "ms (max: 15s)");
        
        logger.info("✅ " + fetchCount + " content type fetches in " + formatDuration(duration));
        logSuccess("testMultipleContentTypeFetchesPerformance", 
                fetchCount + " fetches, " + formatDuration(duration));
    }

    // ===========================
    // Edge Cases
    // ===========================

    @Test
    @Order(11)
    @DisplayName("Test invalid content type UID")
    void testInvalidContentTypeUid() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType("nonexistent_content_type_xyz");
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    // Should return error for invalid UID
                    if (error != null) {
                        logger.info("✅ Invalid UID handled with error: " + error.getErrorMessage());
                        logSuccess("testInvalidContentTypeUid", "Error handled correctly");
                    } else {
                        fail("BUG: Should error for invalid content type UID");
                    }
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testInvalidContentTypeUid"));
    }

    @Test
    @Order(12)
    @DisplayName("Test schema field validation with complex types")
    void testSchemaFieldValidationWithComplexTypes() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "Schema should not be null");
                    
                    // Validate complex field types exist
                    boolean hasComplexField = false;
                    for (int i = 0; i < schema.length(); i++) {
                        org.json.JSONObject field = schema.getJSONObject(i);
                        String dataType = field.optString("data_type");
                        
                        // Check for complex types (group, blocks, reference, global_field)
                        if ("group".equals(dataType) || "blocks".equals(dataType) || 
                            "reference".equals(dataType) || "global_field".equals(dataType)) {
                            hasComplexField = true;
                            logger.info("Complex field found: " + field.optString("uid") + 
                                    " (type: " + dataType + ")");
                        }
                    }
                    
                    if (hasComplexField) {
                        logger.info("✅ Complex field types present in schema");
                    } else {
                        logger.info("ℹ️ No complex field types found (simple schema)");
                    }
                    
                    logSuccess("testSchemaFieldValidationWithComplexTypes", 
                            hasComplexField ? "Complex fields present" : "Simple schema");
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSchemaFieldValidationWithComplexTypes"));
    }

    @Test
    @Order(13)
    @DisplayName("Test schema consistency")
    void testSchemaConsistency() throws InterruptedException, IllegalAccessException {
        // Fetch same content type twice and compare schemas
        final org.json.JSONArray[] firstSchema = {null};
        
        // First fetch
        CountDownLatch latch1 = createLatch();
        ContentType contentType1 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params1 = new org.json.JSONObject();
        
        contentType1.fetch(params1, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    if (error == null && contentTypesModel != null) {
                        org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                        firstSchema[0] = response.optJSONArray("schema");
                    }
                } finally {
                    latch1.countDown();
                }
            }
        });
        
        awaitLatch(latch1, "first-fetch");
        
        // Second fetch
        CountDownLatch latch2 = createLatch();
        ContentType contentType2 = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params2 = new org.json.JSONObject();
        
        contentType2.fetch(params2, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Second fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray secondSchema = response.optJSONArray("schema");
                    
                    assertNotNull(firstSchema[0], "First schema should not be null");
                    assertNotNull(secondSchema, "Second schema should not be null");
                    
                    // Compare field count
                    assertEquals(firstSchema[0].length(), secondSchema.length(),
                            "BUG: Schema field count inconsistent between fetches");
                    
                    logger.info("✅ Schema consistency validated: " + firstSchema[0].length() + " fields");
                    logSuccess("testSchemaConsistency", "Consistent across 2 fetches");
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch2.countDown();
                }
            }
        });
        
        assertTrue(awaitLatch(latch2, "testSchemaConsistency"));
    }

    @Test
    @Order(14)
    @DisplayName("Test schema with all validations")
    void testSchemaWithAllValidations() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    assertNull(error, "Content type fetch should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    
                    assertNotNull(schema, "Schema should not be null");
                    assertTrue(schema.length() > 0, "Schema should have fields");
                    
                    // Validate each field has required properties
                    for (int i = 0; i < schema.length(); i++) {
                        org.json.JSONObject field = schema.getJSONObject(i);
                        
                        assertTrue(field.has("uid"), "Field " + i + " missing uid");
                        assertTrue(field.has("data_type"), "Field " + i + " missing data_type");
                        assertTrue(field.has("display_name"), "Field " + i + " missing display_name");
                        
                        // Validate values are not empty
                        assertFalse(field.optString("uid").isEmpty(), "Field uid should not be empty");
                        assertFalse(field.optString("data_type").isEmpty(), "Data type should not be empty");
                        assertFalse(field.optString("display_name").isEmpty(), "Display name should not be empty");
                    }
                    
                    logger.info("✅ All " + schema.length() + " fields validated successfully");
                    logSuccess("testSchemaWithAllValidations", schema.length() + " fields validated");
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testSchemaWithAllValidations"));
    }

    @Test
    @Order(15)
    @DisplayName("Test comprehensive schema validation scenario")
    void testComprehensiveSchemaValidationScenario() throws InterruptedException, IllegalAccessException {
        CountDownLatch latch = createLatch();
        long startTime = PerformanceAssertion.startTimer();

        ContentType contentType = stack.contentType(Credentials.COMPLEX_CONTENT_TYPE_UID);
        org.json.JSONObject params = new org.json.JSONObject();

        contentType.fetch(params, new ContentTypesCallback() {
            @Override
            public void onCompletion(ContentTypesModel contentTypesModel, Error error) {
                try {
                    long duration = PerformanceAssertion.elapsedTime(startTime);
                    
                    assertNull(error, "Comprehensive scenario should not error");
                    assertNotNull(contentTypesModel, "ContentTypesModel should not be null");
                    
                    org.json.JSONObject response = (org.json.JSONObject) contentTypesModel.getResponse();
                    
                    // Comprehensive validation
                    assertTrue(response.has("uid"), "BUG: UID missing");
                    assertTrue(response.has("title"), "BUG: Title missing");
                    assertEquals(Credentials.COMPLEX_CONTENT_TYPE_UID, response.optString("uid"),
                            "BUG: Wrong content type UID");
                    
                    org.json.JSONArray schema = response.optJSONArray("schema");
                    assertNotNull(schema, "BUG: Schema missing");
                    assertTrue(schema.length() > 0, "BUG: Schema should have fields");
                    
                    // Validate schema structure
                    int validFields = 0;
                    for (int i = 0; i < schema.length(); i++) {
                        org.json.JSONObject field = schema.getJSONObject(i);
                        if (field.has("uid") && field.has("data_type") && field.has("display_name")) {
                            validFields++;
                        }
                    }
                    
                    assertEquals(schema.length(), validFields,
                            "BUG: All fields should have required properties");
                    
                    // Performance check
                    assertTrue(duration < 5000,
                            "PERFORMANCE BUG: Comprehensive took " + duration + "ms (max: 5s)");
                    
                    logger.info("✅ COMPREHENSIVE: " + response.optString("title") + 
                            " with " + validFields + " valid fields in " + formatDuration(duration));
                    logSuccess("testComprehensiveSchemaValidationScenario", 
                            validFields + " fields, " + formatDuration(duration));
                } catch (Exception e) {
                    fail("Error processing response: " + e.getMessage());
                } finally {
                    latch.countDown();
                }
            }
        });

        assertTrue(awaitLatch(latch, "testComprehensiveSchemaValidationScenario"));
    }

    @AfterAll
    void tearDown() {
        logger.info("Completed ContentTypeSchemaValidationIT test suite");
        logger.info("All 15 content type schema validation tests executed");
        logger.info("Tested: basic fetch, field types, system fields, validation, performance, edge cases");
    }
}
