package com.contentstack.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.Map;

// SuperEntry is used as the common class that caould be consumed by Entry and Query
public class SuperEntry {


    /**
     * Merge "source" into "target". If fields have equal name, merge them recursively. Null values in source will
     * remove the field from the target. Override target values with source values Keys not supplied in source will
     * remain unchanged in target
     *
     * @return the merged object (target).
     */
    public static JsonObject deepMerge(JsonObject source, JsonObject target) {

        for (Map.Entry<String, JsonElement> sourceEntry : source.entrySet()) {
            String key = sourceEntry.getKey();
            JsonElement value = sourceEntry.getValue();
            if (!target.has(key)) {
                //target does not have the same key, so perhaps it should be added to target
                if (!value.isJsonNull()) //well, only add if the source value is not null
                    target.add(key, value);
            } else {
                if (!value.isJsonNull()) {
                    if (value.isJsonObject()) {
                        //source value is json object, start deep merge
                        deepMerge(value.getAsJsonObject(), target.get(key).getAsJsonObject());
                    } else {
                        target.add(key, value);
                    }
                } else {
                    target.remove(key);
                }
            }
        }
        return target;
    }


    /**
     * simple test
     */
    public static void main(String[] args) {
        JsonParser parser = new JsonParser();
        JsonObject sourse = parser.parse("{offer: {issue1: null, issue2: null}, accept: true, reject: null}").getAsJsonObject();
        JsonObject target = parser.parse("{offer: {issue2: value2}, reject: false}").getAsJsonObject();
        System.out.println(deepMerge(sourse, target));
    }


}
