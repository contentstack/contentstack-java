package com.builtio.contentstack;

import java.util.LinkedHashMap;



/**
 * To fetch stack level information of your application from Built.io Contentstack server.
 * @author built.io, Inc
 */
public class Stack {

    private static final String TAG = "Stack";
    private String stackApiKey = null;
    protected LinkedHashMap<String, Object> localHeader = null;

    //TODO CONSTANTS
    protected String URLSCHEMA     = "https://";
    protected String URL           = "cdn.contentstack.io";
    protected String VERSION       = "v3";
    protected Config config;

    private Stack(){}

    protected Stack(String stackApiKey) {
        this.stackApiKey = stackApiKey;
        this.localHeader = new LinkedHashMap<>();

    }

    protected void setConfig(Config config){
        this.config        = config;
        URLSCHEMA          = config.URLSCHEMA;
        URL                = config.URL;
        VERSION            = config.VERSION;

        if(!config.environment.isEmpty()){
            setHeader("environment", config.environment);
        }

    }


    public ContentType contentType(String contentTypeName){
        ContentType contentType = new ContentType(contentTypeName);
        contentType.setStackInstance(this);
        return contentType;
    }


    public Asset asset(String uid){
        Asset asset = new Asset(uid);
        asset.setStackInstance(this);

        return asset;
    }


    protected Asset asset(){
        Asset asset = new Asset();
        asset.setStackInstance(this);

        return asset;
    }


    public AssetLibrary assetLibrary(){
        AssetLibrary library = new AssetLibrary();
        library.setStackInstance(this);

        return library;
    }


    public String getApplicationKey(){ return stackApiKey;}


    public String getAccessToken(){ return localHeader != null ? (String)localHeader.get("access_token") : null;};


    public void removeHeader(String key){
        if(!key.isEmpty()){
            localHeader.remove(key);
        }
    }


    public void setHeader(String key, String value) {
        if (!key.isEmpty() && !value.isEmpty()) {
            localHeader.put(key, value);
        }
    }

}

