package com.contentstack.sdk;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class GlobalFieldsIT {

    private GlobalFieldsModel globalFieldsModel;
    private final Stack stack = Credentials.getStack();

    @BeforeEach
    void setUp() {
        globalFieldsModel = new GlobalFieldsModel();
    }

    @Test
    void testSetJSONWithNull() {
        globalFieldsModel.setJSON(null);
        assertNull(globalFieldsModel.getResponse());
        assertEquals(0, globalFieldsModel.getResultArray().length());
    }

    @Test
    void testSetJSONWithEmptyObject() {
        globalFieldsModel.setJSON(new JSONObject());
        assertNull(globalFieldsModel.getResponse());
        assertEquals(0, globalFieldsModel.getResultArray().length());
    }

    @Test
    void testFetchGlobalFieldByUid() throws IllegalAccessException {
        GlobalField globalField = stack.globalField("specific_gf_uid");
        globalField.fetch(new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel model, Error error) {
                JSONArray resp = model.getResultArray();
                Assertions.assertTrue(resp.isEmpty());
            }
        });
    }

    @Test
    void testFindGlobalFieldsIncludeBranch() {
        GlobalField globalField = stack.globalField().includeBranch();
        globalField.findAll(new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel globalFieldsModel, Error error) {
                assertTrue(globalFieldsModel.getResultArray() instanceof JSONArray);
                assertNotNull(((JSONArray) globalFieldsModel.getResponse()).length());
            }
        });
    }

    @Test
    void testFindGlobalFields() throws IllegalAccessException {
        GlobalField globalField = stack.globalField().includeBranch();
        globalField.findAll(new GlobalFieldsCallback() {
            @Override
            public void onCompletion(GlobalFieldsModel globalFieldsModel, Error error) {
                assertTrue(globalFieldsModel.getResultArray() instanceof JSONArray);
                assertNotNull(((JSONArray) globalFieldsModel.getResponse()).length());
            }
        });
    }

    @Test
    void testGlobalFieldSetHeader() throws IllegalAccessException {
        GlobalField globalField = stack.globalField("test_uid");
        globalField.setHeader("custom-header", "custom-value");
        assertNotNull(globalField.headers);
        assertTrue(globalField.headers.containsKey("custom-header"));
        assertEquals("custom-value", globalField.headers.get("custom-header"));
    }

    @Test
    void testGlobalFieldRemoveHeader() throws IllegalAccessException {
        GlobalField globalField = stack.globalField("test_uid");
        globalField.setHeader("test-header", "test-value");
        assertTrue(globalField.headers.containsKey("test-header"));
        
        globalField.removeHeader("test-header");
        assertFalse(globalField.headers.containsKey("test-header"));
    }

    @Test
    void testGlobalFieldIncludeBranch() throws IllegalAccessException {
        GlobalField globalField = stack.globalField("test_uid");
        globalField.includeBranch();
        assertNotNull(globalField.params);
        assertTrue(globalField.params.has("include_branch"));
        assertEquals(true, globalField.params.get("include_branch"));
    }

    @Test
    void testGlobalFieldIncludeSchema() throws IllegalAccessException {
        GlobalField globalField = stack.globalField();
        globalField.includeGlobalFieldSchema();
        assertNotNull(globalField.params);
        assertTrue(globalField.params.has("include_global_field_schema"));
        assertEquals(true, globalField.params.get("include_global_field_schema"));
    }

    @Test
    void testGlobalFieldChainedMethods() throws IllegalAccessException {
        GlobalField globalField = stack.globalField();
        globalField.includeBranch().includeGlobalFieldSchema();
        
        assertTrue(globalField.params.has("include_branch"));
        assertTrue(globalField.params.has("include_global_field_schema"));
        assertEquals(2, globalField.params.length());
    }
}