package com.contentstack.sdk;

import com.contentstack.sdk.Utility.CSAppConstants;

/**
 * Set Configuration for stack instance creation.
 *
 * @author  built.io. Inc
 *
 */
public class Config {

    protected String URLSCHEMA           = "https://";
    protected String URL                 = "cdn.contentstack.io";
    protected String VERSION             = "v3";
    protected String CONTENT_TYPE        = "content_types";
    protected String environment         =  null;
    protected String REQUEST_METHOD_GET  = "GET";
    protected String REQUEST_METHOD_POST = "POST";


    /**
     * BuiltConfig constructor
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * BuiltConfig config = new BuiltConfig();
     * </pre>
     */

    public Config(){}

    /**
     * Sets host name of the Built.io Contentstack server.
     *
     * @param hostName
     * host name.
     *
     * <p>
     * <b>Note:</b> Default hostname sets to <a href ="https://cdn.contentstack.io"> cdn.contentstack.io </a>
     *  and default protocol is HTTPS.
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * config.setHost("cdn.contentstack.io");
     * </pre>
     */

    public void setHost(String hostName){
        if(!hostName.isEmpty()) {
            URL = hostName;
        }
    }

    /**
     * Sets the protocol in base url.
     *
     * @param isSSL
     * 					true/false values initiating calls (HTTPS/HTTP) respectively.
     *
     * <p>
     * <b>Note:</b> Default protocol is HTTPS.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * config.setSSL(true);
     * </pre>
     */

    public void setSSL(boolean isSSL){
        if(isSSL){
            URLSCHEMA = CSAppConstants.URLSCHEMA_HTTPS;
        }else{
            URLSCHEMA = CSAppConstants.URLSCHEMA_HTTP;
        }
    }


    public String getURL(){
        return URL;
    }

    public String getCONTENT_TYPE(){
        return CONTENT_TYPE;
    }

    public String getRequestMethod(){
        return REQUEST_METHOD_GET;
    }

    public String postRequestMethod(){
        return REQUEST_METHOD_POST;
    }

    /**
     * Get URL.
     *
     *
     * @return String @getHost
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String url = config.getHost();
     * </pre>
     */

    public String getHost(){
        return URL;
    }


    /**
     * Get URL.
     *
     *  @return boolean @isSSL
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * boolean protocol = config.isSSL();
     * </pre>
     */

    public boolean isSSL(){ return URLSCHEMA.equalsIgnoreCase(CSAppConstants.URLSCHEMA_HTTPS) ? true : false; }
    /**
     * Get version of the Built.io Contentstack server.
     *
     * @return String @VERSION
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String version = config.getVersion();
     * </pre>
     */
    public String getVersion(){
        return  VERSION;
    }



    /**
     * Changes the Built.io Contentstack version to be used in the final URL.
     *
     * @param version
     *                  version string.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *      config.setVersion("v3");
     * </pre>
     */
    private void setVersion(String version){ if(!version.isEmpty()){
            VERSION = version;
        }
    }

    /**
     * set environment.
     *
     * @param environment
     *                      environment uid/name
     *
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  config.setEnvironment("stag", false);
     * </pre>
     */
    protected void setEnvironment(String environment){
        if(!environment.isEmpty()){
            this.environment = environment;
        }
    }


    /**
     * Get environment.
     *
     * @return String @environment
     *
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  String environment = config.getEnvironment();
     * </pre>
     */

    public String getEnvironment(){
        return environment;
    }


}

