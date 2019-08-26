package com.contentstack.sdk;

/**
 * MIT License
 *
 * Copyright (c) 2012 - 2019 Contentstack
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

public class Config {

    protected String URLSCHEMA      = "https://";
    protected String URL            = "cdn.contentstack.io";
    protected String VERSION        = "v3";
    protected String environment    = null;



    /**
     * Config constructor
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Config config = new Config();
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
     * @param version version string.
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

