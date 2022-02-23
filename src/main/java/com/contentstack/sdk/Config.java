package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;

/**
 * The type Config. enables optional parameters while passing from stack
 */
public class Config {

    protected String livePreviewHash = null;
    protected String livePreviewContentType = null;
    protected String host = "cdn.contentstack.io";
    protected String version = "v3";
    protected String scheme = "https://";
    protected String endpoint;
    protected boolean enableLivePreview = false;
    protected String livePreviewHost;
    protected ContentstackRegion region = ContentstackRegion.US;
    protected String managementToken;
    protected String branch;

    public String getBranch() {
        return branch;
    }

    public void setBranch(String branch) {
        this.branch = branch;
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

    protected String setEndpoint(@NotNull String endpoint) {
        this.endpoint = endpoint;
        return this.endpoint;
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
     * The enum Contentstack region. for now contentstack supports US, EU and AZURE_NA
     */
    public enum ContentstackRegion {
        US, EU, AZURE_NA
    }

}
