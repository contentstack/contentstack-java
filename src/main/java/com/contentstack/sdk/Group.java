package com.contentstack.sdk;

import com.contentstack.sdk.utility.ContentstackUtil;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;


public class Group {

    private JSONObject resultJson;
    private Stack stackInstance;


    protected Group(Stack stack, JSONObject jsonObject) {
        resultJson = jsonObject;
        stackInstance = stack;
    }


    /**
     * Get group representation in json
     *
     * @return JSONObject
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONObject json = group.toJSON();
     * </pre>
     */
    public JSONObject toJSON() {
        return resultJson;
    }


    /**
     * Get object value for key.
     *
     * @param key field_uid as key.
     * @return JSONObject
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Object obj = group.get("key");
     * </pre>
     */
    public Object get(String key) {
        try {
            if (resultJson != null && key != null) {
                return resultJson.get(key);
            } else {
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }


    /**
     * Get string value for key.
     *
     * @param key field_uid as key.
     * @return String
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String value = group.getString("key");
     * </pre>
     */
    public String getString(String key) {
        Object value = get(key);
        if (value != null) {
            if (value instanceof String) {
                return (String) value;
            }
        }
        return null;
    }


    /**
     * Get boolean value for key.
     *
     * @param key field_uid as key.
     * @return boolean true or false
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Boolean value = group.getBoolean("key");
     * </pre>
     */
    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value != null) {
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
        }
        return false;
    }


    /**
     * Get {@link JSONArray} value for key
     *
     * @param key field_uid as key.
     * @return JSONArray
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONArray value = group.getJSONArray("key");
     * </pre>
     */
    public JSONArray getJSONArray(String key) {
        Object value = get(key);
        if (value != null) {
            if (value instanceof JSONArray) {
                return (JSONArray) value;
            }
        }
        return null;
    }


    /**
     * Get {@link JSONObject} value for key
     *
     * @param key field_uid as key.
     * @return JSONObject
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONObject value = group.getJSONObject("key");
     * </pre>
     */
    public JSONObject getJSONObject(String key) {
        Object value = get(key);
        if (value != null) {
            if (value instanceof JSONObject) {
                return (JSONObject) value;
            }
        }
        return null;
    }


    /**
     * Get {@link JSONObject} value for key
     *
     * @param key field_uid as key.
     * @return Number
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * JSONObject value = group.getJSONObject("key");
     * </pre>
     */
    public Number getNumber(String key) {
        Object value = get(key);
        if (value != null) {
            if (value instanceof Number) {
                return (Number) value;
            }
        }
        return null;
    }


    /**
     * Get integer value for key
     *
     * @param key field_uid as key.
     * @return int
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * int value = group.getInt("key");
     * </pre>
     */
    public int getInt(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.intValue();
        }
        return 0;
    }


    /**
     * Get integer value for key
     *
     * @param key field_uid as key.
     * @return float
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * float value = group.getFloat("key");
     * </pre>
     */
    public float getFloat(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.floatValue();
        }
        return (float) 0;
    }


    /**
     * Get double value for key
     *
     * @param key field_uid as key.
     * @return double
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * double value = group.getDouble("key");
     * </pre>
     */
    public double getDouble(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.doubleValue();
        }
        return (double) 0;
    }


    /**
     * Get long value for key
     *
     * @param key field_uid as key.
     * @return long
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * long value = group.getLong("key");
     * </pre>
     */
    public long getLong(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.longValue();
        }
        return (long) 0;
    }

    /**
     * Get short value for key
     *
     * @param key field_uid as key.
     * @return short
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * short value = group.getShort("key");
     * </pre>
     */
    public short getShort(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.shortValue();
        }
        return (short) 0;
    }

    /**
     * Get {@link Calendar} value for key
     *
     * @param key field_uid as key.
     * @return {@link java.util.Date}
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Calendar value = group.getDate("key");
     * </pre>
     */
    public Calendar getDate(String key) {

        try {
            String value = getString(key);
            return ContentstackUtil.parseDate(value, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    /**
     * Get an asset from the group
     *
     * @param key field_uid as key.
     * @return Asset object
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Asset asset = group.getAsset("key");
     * </pre>
     */
    public Asset getAsset(String key) {

        JSONObject assetObject = getJSONObject(key);
        return stackInstance.asset().configure(assetObject);
    }


    /**
     * Get an assets from the group. This works with multiple true fields
     *
     * @param key field_uid as key.
     *            <br><br><b>Example :</b><br>
     *            <pre class="prettyprint">
     *            {@code List<Asset> asset = group.getAssets("key"); }
     *            @return ArrayList of {@link Asset}
     *            </pre>
     */
    public List<Asset> getAssets(String key) {
        List<Asset> assets = new ArrayList<>();
        JSONArray assetArray = getJSONArray(key);
        for (int i = 0; i < assetArray.length(); i++) {
            if (assetArray.opt(i) instanceof JSONObject) {
                Asset asset = stackInstance.asset().configure(assetArray.optJSONObject(i));
                assets.add(asset);
            }
        }
        return assets;
    }


    /**
     * Get a group from the group.
     *
     * @param key field_uid as key.
     *            <br><br><b>Example :</b><br>
     *            <pre class="prettyprint">
     *            Group innerGroup = group.getGroup("key");
     *            @return Group object
     *            </pre>
     */
    public Group getGroup(String key) {
        if (!key.isEmpty() && resultJson.has(key) && resultJson.opt(key) instanceof JSONObject) {
            return new Group(stackInstance, resultJson.optJSONObject(key));
        }
        return null;
    }


    /**
     * Get a list of group from the group.
     * <p>
     * <b>Note :-</b> This will work when group is multiple true.
     *
     * @param key field_uid as key.
     *            <br><br><b>Example :</b><br>
     *            <pre class="prettyprint">
     *            Group innerGroup = group.getGroups("key");
     *            @return List of {@link Group}
     *            </pre>
     */
    public List<Group> getGroups(String key) {

        if (!key.isEmpty() && resultJson.has(key) && resultJson.opt(key) instanceof JSONArray) {
            JSONArray array = resultJson.optJSONArray(key);
            List<Group> groupList = new ArrayList<>();

            for (int i = 0; i < array.length(); i++) {
                if (array.opt(i) instanceof JSONObject) {
                    Group group = new Group(stackInstance, array.optJSONObject(i));
                    groupList.add(group);
                }
            }

            return groupList;
        }
        return null;
    }


    /**
     * Get value for the given reference key.
     *
     * @param refKey         key of a reference field.
     * @param refContentType class uid.
     * @return {@link ArrayList} of {@link Entry} instances.
     * Also specified contentType value will be set as class uid for all {@link Entry} instance.
     */
    public ArrayList<Entry> getAllEntries(String refKey, String refContentType) {
        try {
            if (resultJson != null) {

                if (resultJson.get(refKey) instanceof JSONArray) {

                    int count = ((JSONArray) resultJson.get(refKey)).length();
                    ArrayList<Entry> builtObjectList = new ArrayList<Entry>();
                    for (int i = 0; i < count; i++) {

                        EntryModel model = new EntryModel(((JSONArray) resultJson.get(refKey)).getJSONObject(i), null, false, false, true);
                        Entry entryInstance = null;
                        try {
                            entryInstance = stackInstance.contentType(refContentType).entry();
                        } catch (Exception e) {
                            entryInstance = new Entry(refContentType);
                            e.printStackTrace();
                        }
                        entryInstance.setUid(model.entryUid);
                        entryInstance.ownerEmailId = model.ownerEmailId;
                        entryInstance.ownerUid = model.ownerUid;
                        if (model.ownerMap != null) {
                            entryInstance.owner = new HashMap<>(model.ownerMap);
                        }
                        entryInstance.resultJson = model.jsonObject;
                        entryInstance.setTags(model.tags);

                        builtObjectList.add(entryInstance);
                        model = null;
                    }
                    return builtObjectList;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return null;
    }


}
