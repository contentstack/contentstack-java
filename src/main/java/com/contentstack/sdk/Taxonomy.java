package com.contentstack.sdk;

import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

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
     * You can retrieve filtered entries using taxonomy through two different endpoints:
     *
     * @param queryParams the query parameters should be like below
     *                                       <ul>
     *                                                          <li> <code> <b>IN Operator</b> : Get all entries for a specific taxonomy that satisfy the given conditions provided in the '$in' query.<br>                                                          Your query should be as follows:<br>                                                          <code> {"taxonomies.taxonomy_uid" : { "$in" : ["term_uid1" , "term_uid2" ] }} </code>                                                          </li>                                                          <br>                                                          <li>                                                                <code> <b>OR Operator</b> :                                                                Get all entries for a specific taxonomy that satisfy at least one of the given conditions provided in the “$or” query. <br>                                                                Your query should be as follows: <br>                                                                <code> query={                    $or: [                    { "taxonomies.taxonomy_uid_1" : "term_uid1" },                    { "taxonomies.taxonomy_uid_2" : "term_uid2" }                    ]                    }</code>
     *                                                          </li>
     *                                       <br>
     *                                                          <li>
     *                                                              <b>AND Operator :</b><br>
     *                                                                  Get all entries for a specific taxonomy that satisfy all the conditions provided in the “$and” query.<br>
     *                    <p>
     *                                                                  Your query should be as follows: <br>
     *                                                          <code>
     *                                                          {
     *                                         $and: [
     *                                           { "taxonomies.taxonomy_uid_1" : "term_uid1" },
     *                                           { "taxonomies.taxonomy_uid_2" : "term_uid2" }
     *                                         ]
     *                                       }
     *
     *                                                          </code>
     *                                                          </li>
     *                                       <br>
     *
     *                                       <li>
     *                                        <b>Exists Operator :</b>   <br>
     *                                        Get all entries for a specific taxonomy that if the value of the field, mentioned in the condition, exists. <br>
     *                    <p>
     *                    Your query should be as follows:
     *                                       <code>
     *                                       <code>{"taxonomies.taxonomy_uid" : { "$exists": true }}
     *                                       </code>
     *                                       </li><br>
     *                                       <li>
     *                                              <b>Equal and Below Operator :</b>   <br>
     *                                              Get all entries for a specific taxonomy that match a specific term and all its descendant terms, requiring only the target term and a specified level. <br>
     *                    <p>
     *                          Your query should be as follows:
     *                                             <code>
     *                                             <code>{
     *                      "taxonomies.taxonomy_uid" : { "$eq_below": "term_uid", "level" : 2}}
     *                                             </code>
     *                                             </li>
     *
     *
     *                                       </ul>
     * @return instance of {@link Taxonomy}
     */
    public Taxonomy query(Map<String, Object> queryParams) {
        query.putAll(queryParams);
        return this;
    }

    /**
     * Make request call.
     *
     * @return the call
     */
    Call<ResponseBody> makeRequest() {
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


