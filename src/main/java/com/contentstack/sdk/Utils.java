package com.contentstack.sdk;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.lang.reflect.Field;
import java.util.Map;

public class Utils {

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


    public <T> T merge(T local, T remote) throws IllegalAccessException, InstantiationException {
        Class<?> clazz = local.getClass();
        Object merged = clazz.newInstance();
        for (Field field : clazz.getDeclaredFields()) {
            field.setAccessible(true);
            Object localValue = field.get(local);
            Object remoteValue = field.get(remote);
            if (localValue != null) {
                switch (localValue.getClass().getSimpleName()) {
                    case "Default":
                    case "Detail":
                        field.set(merged, this.merge(localValue, remoteValue));
                        break;
                    default:
                        field.set(merged, (remoteValue != null) ? remoteValue : localValue);
                }
            }
        }
        return (T) merged;
    }



}
