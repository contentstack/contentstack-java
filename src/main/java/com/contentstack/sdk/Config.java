package com.contentstack.sdk;

/**
 * @author  Contentstack.com
 *
 */
public class Config {

    protected String URLSCHEMA      = "https://";
    protected String URL            = "cdn.contentstack.io";
    protected String VERSION        = "v3";
    protected String environment    = null;

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
     * Sets host name of the Contentstack server.
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
        if(hostName != null && !hostName.isEmpty()) {
            URL = hostName;
        }
    }



    /**
     *
     * @return URL String
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String url = config.getHost();
     * </pre>
     */
    public String getHost(){
        return URL;
    }


    /**
     * Get version of the Contentstack server.
     * @return VERSION String
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String version = config.getVersion();
     * </pre>
     */
    public String getVersion(){
        return  VERSION;
    }

    /**
     * Changes the Contentstack version to be used in the final URL.
     *
     * @param version
     *                  version string.
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *      config.setVersion("v3");
     * </pre>
     */
    private void setVersion(String version){

        if(version != null && !version.isEmpty()) {
            VERSION = version;
        }

    }

    /**
     * set environment.
     *
     * @param environment uid/name
     *
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  config.setEnvironment("stag", false);
     * </pre>
     */
    protected void setEnvironment(String environment){
        if(environment != null && !environment.isEmpty()) {
            this.environment = environment;
        }

    }

    /**
     * Get environment.
     * @return param environment string
     *  <br><br><b>Example :</b><br>
     *  <pre class="prettyprint">
     *  String environment = config.getEnvironment();
     * </pre>
     */
    public String getEnvironment(){
        return environment;
    }

}

