package com.contentstack.sdk;

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
    protected String host = "cdn.contentstack.io";
    protected String version = "v3";
    protected String scheme = "https://";
    protected String endpoint;
    protected boolean enableLivePreview = false;
    protected String livePreviewHost;
    protected JSONObject livePreviewEntry = null;
    protected ContentstackRegion region = ContentstackRegion.US;
    protected String managementToken;
    protected String branch;
    protected Proxy proxy = null;
    protected ConnectionPool connectionPool = new ConnectionPool();

    protected List<ContentstackPlugin> plugins = null;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
    }

    /**
     * Proxy can be set like below.
     *
     * @param proxy
     *         Proxy setting, typically a type (http, socks) and a socket address. A Proxy is an immutable object
     *         <br>
     *         <br>
     *         <b>Example:</b><br>
     *         <br>
     *         <code>
     *         java.net.Proxy proxy = new Proxy(Proxy.Type.HTTP,  new InetSocketAddress("proxyHost", "proxyPort"));
     *         java.net.Proxy proxy = new Proxy(Proxy.Type.HTTP, new InetSocketAddress("sl.theproxyvpn.io", 80)); Config
     *         config = new Config(); config.setProxy(proxy);
     *         </code>
     */
    public void setProxy(Proxy proxy) {
        this.proxy = proxy;
    }

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
     * @param maxIdleConnections
     *         the maxIdleConnections default value is 5
     * @param keepAliveDuration
     *         the keepAliveDuration default value is 5
     * @param timeUnit
     *         the timeUnit default value is TimeUnit. MINUTES
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
     * @param region
     *         the region
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
     * Gets host.
     *
     * @return the host
     */
    public String getHost() {
        return host;
    }

    /**
     * Sets host.
     *
     * @param hostName
     *         the host name
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
     * @param enableLivePreview
     *         to enable live preview
     * @return the config
     */
    public Config enableLivePreview(boolean enableLivePreview) {
        this.enableLivePreview = enableLivePreview;
        return this;
    }

    /**
     * Sets live preview host.
     *
     * @param livePreviewHost
     *         the live preview host
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
     * @param managementToken
     *         the management token
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

}
