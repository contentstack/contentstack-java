package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;

/**
 * Configuration Support for contentstack
 */

public class Config {

    protected String livePreviewHash = null;
    protected String livePreviewContentType = null;
    protected String URL_SCHEMA = "https://";
    protected String URL = "cdn.contentstack.io";
    protected String VERSION = "v3";
    protected boolean enableLivePreview = false;
    protected String livePreviewHost;
    protected ContentstackRegion region = ContentstackRegion.US;
    protected String managementToken;

    /**
     * Config constructor
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * Config config = new Config();
     * </pre>
     */

    public Config() {
    }

    public ContentstackRegion getRegion() {
        return this.region;
    }

    /**
     * Sets region allow you to set your region for the Contentstack server.
     *
     * @param region type {@link ContentstackRegion}
     * @return ContentstackRegion
     *
     * <p>
     * <b>Note:</b>
     * Default region sets to us
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * config.setRegion(ContentstackRegion.US);
     * </pre>
     */

    public ContentstackRegion setRegion(ContentstackRegion region) {
        this.region = region;
        return this.region;
    }

    /**
     * @return URL String
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String url = config.getHost();
     * </pre>
     */
    public String getHost() {
        return URL;
    }

    /**
     * Sets host name of the Contentstack server.
     *
     * @param hostName host name.
     *
     *<p>
     *<b>Note:</b> Default hostname sets to <a href ="https://cdn.contentstack.io"> cdn.contentstack.io </a>
     *and default protocol is HTTPS.
     *<br><br><b>Example :</b><br>
     *<pre class="prettyprint">
     *config.setHost("cdn.contentstack.io");
     *</pre>
     */

    public void setHost(String hostName) {
        if (hostName != null && !hostName.isEmpty()) {
            URL = hostName;
        }
    }

    /**
     * Get version of the Contentstack server.
     *
     * @return VERSION String
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     * String version = config.getVersion();
     * </pre>
     */
    public String getVersion() {
        return VERSION;
    }

    /**
     * Changes the Contentstack version to be used in the final URL.
     *
     * @param version version string.
     *
     *<br><br><b>Example :</b><br>
     *<pre class="prettyprint">
     *config.setVersion("v3");
     *</pre>
     */
    private void setVersion(String version) {
        if (version != null && !version.isEmpty()) {
            VERSION = version;
        }
    }


    // Live preview enabler
    public Config enableLivePreview(boolean enableLivePreview) {
        // It enables the livePreview
        this.enableLivePreview = enableLivePreview;
        return this;
    }

    /**
     * Sets live preview host.
     *
     * @param livePreviewHost the live preview host
     * @return the live preview host
     */
    public Config setLivePreviewHost(@NotNull String livePreviewHost) {
        // It sets the host for the livePreview
        this.livePreviewHost = livePreviewHost;
        return this;
    }

    /**
     * Sets authorization.
     *
     * @param managementToken the management token
     * @return the authorization
     */
    public Config setManagementToken(@NotNull String managementToken) {
        // It sets Management Token for the livePreview
        this.managementToken = managementToken;
        return this;
    }


    public enum ContentstackRegion {US, EU}

}

