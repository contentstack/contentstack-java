package com.builtio.contentstack;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.LinkedHashMap;



/**
 * To fetch stack level information of your application from Built.io Contentstack server.
 * @author built.io, Inc
 */
public class Stack {

    private static final String TAG = "Stack";
    private String stackApiKey = null;
    protected LinkedHashMap<String, Object> localHeader = null;
    private String imageTransformationUrl;
    private LinkedHashMap<String, Object> imageParams = new LinkedHashMap<>();

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



    /**
     * @param image_url
     * on which we want to manipulate.
     * @param parameters
     * It is an second parameter in which we want to place different manipulation key and value in array form
     * @return String
     *
     * ImageTransform function is define for image manipulation with different
     * parameters in second parameter in array form
     *
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  //'blt5d4sample2633b' is a dummy Stack API key
     *  //'blt6d0240b5sample254090d' is dummy access token.
     *  Stack stack = Contentstack.stack(context, "blt5d4sample2633b", "blt6d0240b5sample254090d", "stag", false);<br>
     *  // resize the image by specifying width and height
     *  LinkedHashMap imageParams = new LinkedHashMap();
     *  imageParams.put("width", 100);
     *  imageParams.put("height",100);
     *  imageUrl = Stack.ImageTransform(image_url, parameters);
     *  stack.ImageTransform(image_url, parameters);
     *
     *
     *
     *  </pre>
     *
     */
    public String ImageTransform(String image_url, LinkedHashMap<String, Object> parameters) {
        imageTransformationUrl = image_url;
        imageParams = parameters;
        return getImageUrl();
    }




    private String getImageUrl() {

        if (imageParams == null || imageParams.size() == 0) {
            return imageTransformationUrl;
        }

        imageParams.forEach((key, value) -> {

            try {
                final String encodedKey = URLEncoder.encode(key.toString(), "UTF-8");
                final String encodedValue = URLEncoder.encode(value.toString(), "UTF-8");
                if (!imageTransformationUrl.contains("?")) {
                    imageTransformationUrl += "?" + encodedKey + "=" + encodedValue;
                } else {
                    imageTransformationUrl += "&" + encodedKey + "=" + encodedValue;
                }

            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        });

        return imageTransformationUrl;
    }




}

