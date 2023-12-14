package com.contentstack.sdk;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.HeaderMap;
import retrofit2.http.Query;
import retrofit2.http.Url;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Shailesh Mishra
 * @version 1.0.0
 * @since 12-12-2021
 */
public interface APIService {
    @GET
    Call<ResponseBody> getRequest(
            @Url String url, @HeaderMap LinkedHashMap<String, Object> headers);

    @GET("v3/taxonomies/entries")
    Call<ResponseBody> getTaxonomy(
            @HeaderMap Map<String, Object> headers,
            @Query("query") String query);
}
