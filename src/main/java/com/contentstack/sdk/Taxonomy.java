package com.contentstack.sdk;

import okhttp3.Request;
import okhttp3.ResponseBody;
import org.jetbrains.annotations.NotNull;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The type Taxonomy.
 *
 * @author Shailesh Mishra <br> <b>Taxonomy : </b> <p> Taxonomy, currently in the Early Access Phase simplifies the process of organizing content in your system, making it effortless to find and retrieve information.
 */
public class Taxonomy {


    protected LinkedHashMap<String, Object> headers;
    protected APIService service;
    protected HashMap<String, Object> query = new HashMap<>();
    protected Config config;

    /**
     * Instantiates a new Taxonomy.
     *
     * @param service the service
     * @param config  the config
     * @param headers the headers
     */
    public Taxonomy(APIService service, Config config, LinkedHashMap<String, Object> headers) {
        this.service = service;
        this.headers = headers;
        this.config = config;
    }


    /**
     * Get all entries for a specific taxonomy that satisfy the given conditions provided in the '$in' query.
     * Your query should be as follows:
     * <p>
     * <pre>
     * {"taxonomies.taxonomy_uid" : { "$in" : ["term_uid1" , "term_uid2" ] }}
     * </pre>
     * <p>
     * <b>Example:</b> If you want to retrieve entries with the color taxonomy applied and linked to the term red and/or yellow.
     * <p>
     * <pre>
     * {"taxonomies.color" : { "$in" : ["red" , "yellow" ] }}
     * </pre>
     *
     * @param taxonomy    the key of the taxonomy to query
     * @param listOfItems the list of taxonomy fields
     * @return an instance of the Taxonomy with the specified conditions added to the query
     */
    public Taxonomy in(String taxonomy, String[] listOfItems) {
        String formattedValues = Arrays.stream(listOfItems).map(value -> "\"" + value.trim() + "\"").collect(Collectors.joining(" , "));

        String stringify = "{ \"$in\" : [" + formattedValues + "] }}";
        this.query.put(taxonomy, stringify);
        return this;
    }


    /**
     * <b>OR Operator :</b>
     * <p>
     * Get all entries for a specific taxonomy that satisfy at least one of the given conditions provided in the “$or” query.
     * <p>
     * Your query should be as follows:
     * <p>
     * <pre>
     *
     * { $or: [
     * { "taxonomies.taxonomy_uid_1" : "term_uid1" },
     * { "taxonomies.taxonomy_uid_2" : "term_uid2" }
     * ]}
     *
     * </pre>
     * Example: If you want to retrieve entries with either the color or size taxonomy applied and linked to the terms yellow and small, respectively.
     * <br>
     * <pre>
     *
     * {$or: [
     * { "taxonomies.color" : "yellow" },
     * { "taxonomies.size" : "small" }
     * ]}
     *
     *
     * </pre>
     *
     * @param listOfItems
     * @return
     */
    public Taxonomy or(@NotNull List<HashMap<String, String>> listOfItems) {
        for (int i = 0; i < listOfItems.size(); i++) {
            HashMap<String, String> param = listOfItems.get(i);
            if (i > 0) {
                this.query.put("$or", listOfItems.toArray());
            }
            this.query.put("$or", param);
        }
        return this;
    }


    /**
     * <b>AND Operator :</b>
     * <p>
     * Get all entries for a specific taxonomy that satisfy all the conditions provided in the “$and” query.
     * <p>
     * Your query should be as follows:
     *
     * <pre>
     * {
     * $and: [
     * { "taxonomies.taxonomy_uid_1" : "term_uid1" },
     * { "taxonomies.taxonomy_uid_2" : "term_uid2" }
     * ]
     * }
     * </pre>
     * <b>Example:</b> If you want to retrieve entries with the color and computers taxonomies applied and linked to the terms red and laptop, respectively.
     *
     * <pre>
     * {
     * $and: [
     * { "taxonomies.color" : "red" },
     * { "taxonomies.computers" : "laptop" }
     * ]
     * }
     * </pre>
     *
     * @param listOfItems the list of items to that you want to include in the query string
     * @return instance of the Taxonomy
     */
    public Taxonomy and(@NotNull List<HashMap<String, String>> listOfItems) {
        for (int i = 0; i < listOfItems.size(); i++) {
            HashMap<String, String> param = listOfItems.get(i);
            if (i > 0) {
                this.query.put("$and", listOfItems.toArray());
            }
            this.query.put("$and", param);
        }
        return this;
    }


    /**
     * <b>Exists Operator :</b>
     * <p>
     * Get all entries for a specific taxonomy that if the value of the field, mentioned in the condition, exists.
     * <p>
     * Your query should be as follows:
     * <pre>
     * {"taxonomies.taxonomy_uid" : { "$exists": true }}
     * </pre>
     * Example: If you want to retrieve entries with the color taxonomy applied.
     * <pre>
     * {"taxonomies.color" : { "$exists": true }}
     * </pre>
     *
     * @param taxonomy the taxonomy
     * @param value    the value of the field
     * @return instance of Taxonomy
     */
    public Taxonomy exists(@NotNull String taxonomy, @NotNull Boolean value) {
        HashMap<String, Boolean> param = new HashMap<>();
        param.put("$exists", value);
        this.query.put(taxonomy, param);
        return this;
    }


    /**
     * <b>Equal and Below Operator :</b>
     * <p>
     * Get all entries for a specific taxonomy that match a specific term and all its descendant terms, requiring only the target term and a specified level.
     * <p>
     * Note: If you don't specify the level, the default behavior is to retrieve terms up to level 10.
     *
     * <pre>{"taxonomies.taxonomy_uid" : { "$eq_below": "term_uid", "level" : 2}}</pre>
     *
     * <b>Example:</b> If you want to retrieve all entries with terms nested under blue, such as navy blue and sky blue, while also matching entries with the target term blue.
     *
     * <pre>{"taxonomies.color" : { "$eq_below": "blue" }}</pre>
     *
     * @param taxonomy the taxonomy
     * @param termsUid the term uid
     * @return instance of Taxonomy
     */
    public Taxonomy equalAndBelow(@NotNull String taxonomy, @NotNull String termsUid) {
        HashMap<String, String> param = new HashMap<>();
        param.put("$eq_below", termsUid);
        this.query.put(taxonomy, param);
        return this;
    }

    /**
     * Note: If you don't specify the level, the default behavior is to retrieve terms up to level 10.
     *
     * @param taxonomy the taxonomy
     * @param termsUid the terms
     * @param level    the level to retrieve terms up to mentioned level
     * @return instance of Taxonomy
     */
    public Taxonomy equalAndBelowWithLevel(@NotNull String taxonomy, @NotNull String termsUid, @NotNull int level) {
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("$eq_below", termsUid + ", level: " + level);
        this.query.put(taxonomy, innerMap);
        return this;
    }


    /**
     * <b>Below Operator</b>
     * <br>
     * <p>
     * Get all entries for a specific taxonomy that match all of their descendant terms by specifying only the target term and a specific level.
     * <br>
     * <b>Note:</b> If you don't specify the level, the default behavior is to retrieve terms up to level 10.
     * <br>
     * <pre>{"taxonomies.taxonomy_uid" : { "$below": "term_uid", "level" : 2}}</pre>
     *
     * <b>Example:</b> If you want to retrieve all entries containing terms nested under blue, such as navy blue and sky blue, but exclude entries that solely have the target term blue.
     *
     * <pre>{"taxonomies.color" : { "$below": "blue" }}</pre>
     *
     * @param taxonomy the taxonomy
     * @param termsUid the  terms uid
     * @return instance of Taxonomy
     */
    public Taxonomy below(@NotNull String taxonomy, @NotNull String termsUid) {
        HashMap<String, String> param = new HashMap<>();
        param.put("$below", termsUid);
        this.query.put(taxonomy, param);
        return this;
    }


    /**
     * <b>Equal and Above Operator :</b>
     * <p>
     * Get all entries for a specific taxonomy that match a specific term and all its ancestor terms, requiring only the target term and a specified level.
     *
     * <b>Note:</b> If you don't specify the level, the default behavior is to retrieve terms up to level 10.
     * <p>
     * <pre>{"taxonomies.taxonomy_uid": { "$eq_above": "term_uid", "level": 2 }}</pre>
     * <p>
     * Example: If you want to obtain all entries that include the term led and its parent term tv.
     * <p>
     * <pre>{"taxonomies.appliances": { "$eq_above": "led"}}</pre>
     *
     * @param taxonomy the taxonomy
     * @param termUid  the term uid
     * @return instance of Taxonomy
     */
    public Taxonomy equalAbove(@NotNull String taxonomy, @NotNull String termUid) {
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("$eq_above", termUid);
        this.query.put(taxonomy, innerMap);
        return this;
    }


    /**
     * <b>Above Operator :</b>
     * <p>
     * Get all entries for a specific taxonomy that match only the parent term(s) of a specified target term, excluding the target term itself. You can also specify a specific level.
     * <p>
     * Note: If you don't specify the level, the default behavior is to retrieve terms up to level 10.
     *
     * <pre>{ "taxonomies.taxonomy_uid": { "$above": "term_uid", "level": 2 }}</pre>
     * <p>
     * Example: If you wish to match entries with the term tv but exclude the target term led.
     *
     * <pre>{"taxonomies.appliances": { "$above": "led" }}</pre>
     *
     * @param taxonomy the taxonomy
     * @param termUid  the term uid
     * @return instance of {@link Taxonomy}
     */
    public Taxonomy above(@NotNull String taxonomy, @NotNull String termUid) {
        Map<String, Object> innerMap = new HashMap<>();
        innerMap.put("$above", termUid);
        this.query.put(taxonomy, innerMap);
        return this;
    }


    /**
     * To verify the payload
     *
     * @return instance of Call<ResponseBody>
     */
    protected Call<ResponseBody> makeRequest() {
        HashMap<String, Object> map = new HashMap<>();
        map.put("query", query);
        return this.service.getTaxonomy(this.headers, map);
    }


    /**
     * Find.
     *
     * @param callback the callback
     */
    public void find(TaxonomyCallback callback) {
        try {
            Response<ResponseBody> response = makeRequest().execute();
            if (response.isSuccessful()) {
                callback.onResponse(response.body());
            } else {
                Request request = makeRequest().request();
                callback.onFailure(request, response.errorBody());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}


