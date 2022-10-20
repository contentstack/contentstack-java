package com.contentstack.sdk;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class Group {

    protected static final Logger logger = Logger.getLogger(Group.class.getSimpleName());
    private final JSONObject resultJson;
    private final Stack stackInstance;
    protected APIService service;

    protected Group(Stack stack, JSONObject jsonObject) {
        this.service = stack.service;
        resultJson = jsonObject;
        stackInstance = stack;
    }

    /**
     * Get group representation in json
     *
     * @return JSONObject <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         JSONObject json = group.toJSON();
     *         </pre>
     */
    public JSONObject toJSON() {
        return resultJson;
    }

    /**
     * Get object value for key.
     *
     * @param key
     *            field_uid as key.
     * @return JSONObject <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Object obj = group.get("key");
     *         </pre>
     */
    public Object get(String key) {
        if (resultJson != null && key != null) {
            return resultJson.get(key);
        } else {
            return null;
        }
    }

    /**
     * Get string value for key.
     *
     * @param key
     *            field_uid as key.
     * @return String <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         String value = group.getString("key");
     *         </pre>
     */
    public String getString(String key) {
        Object value = get(key);
        if (value != null) {
            return (String) value;
        }
        return null;
    }

    /**
     * Get boolean value for key.
     *
     * @param key
     *            field_uid as key.
     * @return boolean true or false <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Boolean value = group.getBoolean("key");
     *         </pre>
     */
    public Boolean getBoolean(String key) {
        Object value = get(key);
        if (value != null) {
            return (Boolean) value;
        }
        return false;
    }

    /**
     * Get {@link JSONArray} value for key
     *
     * @param key
     *            field_uid as key.
     * @return JSONArray <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         JSONArray value = group.getJSONArray("key");
     *         </pre>
     */
    public JSONArray getJSONArray(String key) {
        Object value = get(key);
        if (value != null) {
            return (JSONArray) value;
        }
        return null;
    }

    /**
     * Get {@link JSONObject} value for key
     *
     * @param key
     *            field_uid as key.
     * @return JSONObject <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         JSONObject value = group.getJSONObject("key");
     *         </pre>
     */
    public JSONObject getJSONObject(String key) {
        Object value = get(key);
        if (value != null) {
            return (JSONObject) value;
        }
        return null;
    }

    /**
     * Get {@link JSONObject} value for key
     *
     * @param key
     *            field_uid as key.
     * @return Number <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         JSONObject value = group.getJSONObject("key");
     *         </pre>
     */
    public Number getNumber(String key) {
        Object value = get(key);
        if (value != null) {
            return (Number) value;
        }
        return null;
    }

    /**
     * Get integer value for key
     *
     * @param key
     *            field_uid as key.
     * @return int <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         int value = group.getInt("key");
     *         </pre>
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
     * @param key
     *            field_uid as key.
     * @return float <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         float value = group.getFloat("key");
     *         </pre>
     */
    public float getFloat(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.floatValue();
        }
        return 0;
    }

    /**
     * Get double value for key
     *
     * @param key
     *            field_uid as key.
     * @return double <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         double value = group.getDouble("key");
     *         </pre>
     */
    public double getDouble(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.doubleValue();
        }
        return 0;
    }

    /**
     * Get long value for key
     *
     * @param key
     *            field_uid as key.
     * @return long <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         long value = group.getLong("key");
     *         </pre>
     */
    public long getLong(String key) {
        Number value = getNumber(key);
        if (value != null) {
            return value.longValue();
        }
        return 0;
    }

    /**
     * Get short value for key
     *
     * @param key
     *            field_uid as key.
     * @return short <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         short value = group.getShort("key");
     *         </pre>
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
     * @param key
     *            field_uid as key.
     * @return {@link java.util.Date} <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Calendar value = group.getDate("key");
     *         </pre>
     */
    public Calendar getDate(String key) {

        try {
            String value = getString(key);
            return Constants.parseDate(value, null);
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
        }
        return null;
    }

    /**
     * Get an asset from the group
     *
     * @param key
     *            field_uid as key.
     * @return Asset object <br>
     *         <br>
     *         <b>Example :</b><br>
     *
     *         <pre class="prettyprint">
     *         Asset asset = group.getAsset("key");
     *         </pre>
     */
    public Asset getAsset(String key) {
        JSONObject assetObject = getJSONObject(key);
        return stackInstance.asset().configure(assetObject);
    }

    /**
     * Get an assets from the group. This works with multiple true fields
     *
     * @param key
     *            field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *                      {@code List<Asset> asset = group.getAssets("key"); }
     *                      @return ArrayList of {@link Asset}
     *                    </pre>
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
     * @param key
     *            field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *                      Group innerGroup = group.getGroup("key");            &#64;return Group
     *                 object
     *            </pre>
     * 
     * @return the group
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
     * @param key
     *            field_uid as key. <br>
     *            <br>
     *            <b>Example :</b><br>
     *
     *            <pre class="prettyprint">
     *                      Group innerGroup = group.getGroups("key");
     *                 @return List of {@link Group}
     *                    </pre>
     */
    public List<Group> getGroups(String key) {
        List<Group> groupList = new ArrayList<>();
        if (!key.isEmpty() && resultJson.has(key) && resultJson.opt(key) instanceof JSONArray) {
            JSONArray array = resultJson.optJSONArray(key);
            array.forEach(model -> {
                JSONObject newModel = (JSONObject) model;
                Group group = new Group(stackInstance, newModel);
                groupList.add(group);
            });
        }
        return groupList;
    }

    /**
     * Get value for the given reference key.
     *
     * @param refKey
     *                       key of a reference field.
     * @param refContentType
     *                       class uid.
     * @return {@link ArrayList} of {@link Entry} instances. Also specified
     *         contentType value will be set as class uid
     *         for all {@link Entry} instance.
     */
    public ArrayList<Entry> getAllEntries(String refKey, String refContentType) {
        ArrayList<Entry> entryContainer = new ArrayList<>();
        try {

            if (resultJson != null) {
                if (resultJson.get(refKey) instanceof JSONArray) {
                    int count = ((JSONArray) resultJson.get(refKey)).length();
                    for (int i = 0; i < count; i++) {
                        EntryModel model = new EntryModel(((JSONArray) resultJson.get(refKey)).getJSONObject(i));
                        Entry entryInstance = null;
                        try {
                            entryInstance = stackInstance.contentType(refContentType).entry();
                        } catch (Exception e) {
                            entryInstance = new Entry(refContentType);
                            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
                        }
                        entryInstance.setUid(model.uid);
                        entryInstance.resultJson = model.jsonObject;
                        entryInstance.setTags(model.tags);
                        entryContainer.add(entryInstance);
                    }
                    return entryContainer;
                }
            }
        } catch (Exception e) {
            logger.log(Level.SEVERE, e.getLocalizedMessage(), e);
            return entryContainer;
        }
        return entryContainer;
    }

}
