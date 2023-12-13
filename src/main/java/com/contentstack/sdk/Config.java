package com.contentstack.sdk;

import lombok.Getter;
import lombok.Setter;
import okhttp3.ConnectionPool;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;

import java.net.Proxy;
import java.util.List;
import java.util.concurrent.TimeUnit;

/**
 * The Config enables optional parameters while passing from stack. You can set different configs params to the stack
 * like host, version, livePreview, endpoint, region, branch etc
 */
public class Config {

    protected String livePreviewHash = null;
    protected String livePreviewContentType = null;
    protected String livePreviewEntryUid = null;
    @Getter
    protected String host = "cdn.contentstack.io";
    protected String version = "v3";
    protected String scheme = "https://";
    protected String endpoint;
    protected boolean enableLivePreview = false;
    protected String livePreviewHost;
    protected JSONObject livePreviewEntry = null;
    protected ContentstackRegion region = ContentstackRegion.US;
    protected String managementToken;
    @Setter
    @Getter
    protected String branch;
    @Setter
    protected Proxy proxy = null;
    protected ConnectionPool connectionPool = new ConnectionPool();

    protected List<ContentstackPlugin> plugins = null;

    /**
     * -- GETTER --
     * The configuration for the contentstack that contains support for
     */
    @Getter
    protected String[] earlyAccess;

    /**
     * Returns the Proxy instance
     *
     * @return Proxy
     */
    public Proxy getProxy() {
        return this.proxy;
    }

    /**
     * Manages reuse of HTTP and HTTP/2 connections for reduced network latency. HTTP requests that * share the same
     * {@link okhttp3.Address} may share a {@link okhttp3.Connection}. This class implements the policy * of which
     * connections to keep open for future use.
     *
     * @param maxIdleConnections the maxIdleConnections default value is 5
     * @param keepAliveDuration  the keepAliveDuration default value is 5
     * @param timeUnit           the timeUnit default value is TimeUnit. MINUTES
     * @return ConnectionPool
     */
    public ConnectionPool connectionPool(int maxIdleConnections, long keepAliveDuration, TimeUnit timeUnit) {
        this.connectionPool = new ConnectionPool(maxIdleConnections, keepAliveDuration, timeUnit);
        return this.connectionPool;
    }

    /**
     * Gets region.
     *
     * @return the region
     */
    public ContentstackRegion getRegion() {
        return this.region;
    }

    /**
     * Sets region.
     *
     * @param region the region
     * @return the region
     */
    public ContentstackRegion setRegion(ContentstackRegion region) {
        this.region = region;
        return this.region;
    }

    protected String getEndpoint() {
        return endpoint + "/" + getVersion() + "/";
    }

    protected void setEndpoint(@NotNull String endpoint) {
        this.endpoint = endpoint;
    }

    public void setPlugins(List<ContentstackPlugin> plugins) {
        this.plugins = plugins;
    }

    /**
     * Sets host.
     *
     * @param hostName the host name
     */
    public void setHost(String hostName) {
        if (hostName != null && !hostName.isEmpty()) {
            host = hostName;
        }
    }

    /**
     * Gets version.
     *
     * @return the version
     */
    public String getVersion() {
        return version;
    }

    /**
     * Enable live preview config.
     *
     * @param enableLivePreview to enable live preview
     * @return the config
     */
    public Config enableLivePreview(boolean enableLivePreview) {
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
        this.livePreviewHost = livePreviewHost;
        return this;
    }

    protected Config setLivePreviewEntry(@NotNull JSONObject livePreviewEntry) {
        this.livePreviewEntry = livePreviewEntry;
        return this;
    }

    /**
     * Sets management token.
     *
     * @param managementToken the management token
     * @return the management token
     */
    public Config setManagementToken(@NotNull String managementToken) {
        this.managementToken = managementToken;
        return this;
    }

    /**
     * The enum Contentstack region. for now contentstack supports [US, EU, AZURE_NA]
     */
    public enum ContentstackRegion {
        US, EU, AZURE_NA, AZURE_EU
    }


    /**
     * To initialize the SDK with the latest features offered in the early access phase,
     * include the early access parameter as shown in the following code:
     *
     * @param earlyAccessFeatures The list of Early Access Features
     *                            {@code
     *                            Config config = new Config();
     *                            String[] earlyAccess = {"Taxonomy", "Teams", "Terms", "LivePreview"};
     *                            config.earlyAccess(earlyAccess);
     *                            Stack stack = Contentstack.stack(API_KEY, DELIVERY_TOKEN, ENV, config);
     *                            <p>
     *                            }
     * @return Config
     */
    public Config earlyAccess(@NotNull String[] earlyAccessFeatures) {
        this.earlyAccess = earlyAccessFeatures;
        return this;
    }

}
