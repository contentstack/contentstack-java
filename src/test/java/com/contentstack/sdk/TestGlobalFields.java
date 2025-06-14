package com.contentstack.sdk;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class TestGlobalFields {

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
}