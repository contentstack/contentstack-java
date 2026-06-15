package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

/**
 * Resolves Contentstack API endpoints for any region and service without hardcoding host strings.
 *
 * <h3>Resolution chain</h3>
 * <ol>
 *   <li><b>In-memory cache</b> — populated on the first call and reused for the JVM lifetime
 *       (zero I/O on every subsequent call).</li>
 *   <li><b>Bundled {@code regions.json}</b> — read from the classpath resource
 *       {@code /assets/regions.json} that is packaged inside the SDK jar. Works
 *       fully offline with zero latency.</li>
 *   <li><b>Live download</b> — if the requested region is not present in the bundled file
 *       (e.g. Contentstack added a new region after this SDK version was released), a single
 *       HTTP request is made to {@value #REGIONS_URL} to fetch the latest registry. The
 *       downloaded data replaces the in-memory cache so all subsequent lookups benefit from it.
 *       This attempt is made at most <em>once</em> per JVM session to avoid repeated network
 *       calls for genuinely invalid region strings.</li>
 * </ol>
 *
 * <p>Region matching is case-insensitive and treats {@code -} and {@code _} as equivalent
 * separators, so {@code "AZURE_NA"}, {@code "azure-na"}, and {@code "Azure_NA"} all resolve
 * to the same region.
 *
 * <p><b>Examples:</b>
 * <pre>
 *   String url  = Endpoint.getContentstackEndpoint("eu", "contentDelivery");
 *   // → "https://eu-cdn.contentstack.com"
 *
 *   String host = Endpoint.getContentstackEndpoint("eu", "contentDelivery", true);
 *   // → "eu-cdn.contentstack.com"
 *
 *   Map&lt;String, String&gt; all = Endpoint.getAllEndpoints("azure-na");
 *   // → {"contentDelivery": "https://azure-na-cdn.contentstack.com", ...}
 * </pre>
 */
public class Endpoint {

    static final String REGIONS_URL = "https://artifacts.contentstack.com/regions.json";

    private static final Logger logger = Logger.getLogger(Endpoint.class.getSimpleName());

    private static volatile JSONArray regionsCache = null;

    // Ensures the live download is attempted at most once per JVM session so that
    // genuinely invalid region strings do not trigger repeated network calls.
    private static volatile boolean liveRefreshDone = false;

    private Endpoint() {
    }

    /**
     * Returns the URL for the given region and service.
     *
     * @param region  the region ID or alias (e.g. {@code "na"}, {@code "eu"}, {@code "azure-na"})
     * @param service the service key (e.g. {@code "contentDelivery"}, {@code "contentManagement"})
     * @return the full URL including {@code https://} scheme
     * @throws IllegalArgumentException if the region or service is not recognised
     */
    public static String getContentstackEndpoint(@NotNull String region, @NotNull String service) {
        return getContentstackEndpoint(region, service, false);
    }

    /**
     * Returns the URL for the given region and service, optionally stripping the {@code https://}
     * scheme.
     *
     * @param region    the region ID or alias
     * @param service   the service key
     * @param omitHttps when {@code true}, returns the bare host without the {@code https://} prefix
     * @return the URL (or bare host when {@code omitHttps} is {@code true})
     * @throws IllegalArgumentException if the region or service is not recognised
     */
    public static String getContentstackEndpoint(@NotNull String region, @NotNull String service,
            boolean omitHttps) {
        if (region.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty region provided. Please provide a valid region.");
        }
        JSONObject regionRow = resolveRegion(region);
        JSONObject endpoints = regionRow.getJSONObject("endpoints");
        if (!endpoints.has(service)) {
            throw new IllegalArgumentException(
                    "Service \"" + service + "\" not found for region \"" + region + "\"");
        }
        String url = endpoints.getString(service);
        return omitHttps ? stripHttps(url) : url;
    }

    /**
     * Returns all endpoints for the given region as an ordered map of service key to URL.
     *
     * @param region the region ID or alias
     * @return map of service key → URL
     * @throws IllegalArgumentException if the region is not recognised
     */
    public static Map<String, String> getAllEndpoints(@NotNull String region) {
        return getAllEndpoints(region, false);
    }

    /**
     * Returns all endpoints for the given region, optionally stripping the {@code https://} scheme.
     *
     * @param region    the region ID or alias
     * @param omitHttps when {@code true}, returns bare hosts without the {@code https://} prefix
     * @return map of service key → URL (or bare host)
     * @throws IllegalArgumentException if the region is not recognised
     */
    public static Map<String, String> getAllEndpoints(@NotNull String region, boolean omitHttps) {
        if (region.trim().isEmpty()) {
            throw new IllegalArgumentException("Empty region provided. Please provide a valid region.");
        }
        JSONObject regionRow = resolveRegion(region);
        JSONObject endpoints = regionRow.getJSONObject("endpoints");
        Map<String, String> result = new LinkedHashMap<>();
        for (String key : endpoints.keySet()) {
            String url = endpoints.getString(key);
            result.put(key, omitHttps ? stripHttps(url) : url);
        }
        return result;
    }

    /**
     * Resets the in-memory cache and the live-refresh flag. Intended for testing only.
     */
    static synchronized void resetCache() {
        regionsCache = null;
        liveRefreshDone = false;
    }

    // ── internals ─────────────────────────────────────────────────────────────

    /**
     * Resolution chain:
     * <ol>
     *   <li>In-memory cache</li>
     *   <li>Bundled classpath {@code regions.json}</li>
     *   <li>Live download (once per JVM session, triggered only when the region is absent)</li>
     * </ol>
     */
    private static JSONObject resolveRegion(String region) {
        // Tier 1 + 2: load from cache or bundled classpath
        JSONArray regions = loadRegions();
        try {
            return findRegion(regions, region);
        } catch (IllegalArgumentException notInBundled) {
            // Tier 3: region not in bundled file — attempt one live refresh.
            // This handles the case where Contentstack added a new region after this
            // SDK version was released and the user hasn't upgraded yet.
            if (!liveRefreshDone) {
                JSONArray fresh = tryLiveRefresh();
                if (fresh != null) {
                    try {
                        return findRegion(fresh, region);
                    } catch (IllegalArgumentException ignored) {
                        // Region absent even in the live data → fall through and throw below
                    }
                }
            }
            throw notInBundled;
        }
    }

    /**
     * Loads regions from the in-memory cache or the bundled classpath resource.
     * Populates the cache on the first call.
     */
    private static synchronized JSONArray loadRegions() {
        if (regionsCache != null) {
            return regionsCache;
        }
        // Try bundled classpath resource (packaged inside the SDK jar)
        InputStream stream = Endpoint.class.getResourceAsStream("/assets/regions.json");
        if (stream != null) {
            try (Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name())) {
                String raw = scanner.useDelimiter("\\A").next();
                JSONObject root = new JSONObject(raw);
                regionsCache = root.getJSONArray("regions");
                return regionsCache;
            }
        }
        // Bundled file absent (e.g. corrupted build) — try live download immediately
        logger.warning("Bundled regions.json not found in classpath — attempting live download.");
        JSONArray downloaded = tryLiveRefresh();
        if (downloaded != null) {
            return downloaded;
        }
        throw new IllegalStateException(
                "regions.json not found in classpath and could not be downloaded from "
                        + REGIONS_URL + ". Ensure the SDK jar was built correctly, or check network access.");
    }

    /**
     * Attempts a one-time HTTP fetch of {@value #REGIONS_URL}.
     * Returns the parsed regions array on success, or {@code null} if the attempt is skipped
     * or the network is unavailable. Updates the in-memory cache on success.
     */
    private static synchronized JSONArray tryLiveRefresh() {
        if (liveRefreshDone) {
            return regionsCache; // already fetched this session — return whatever we have
        }
        liveRefreshDone = true;
        try {
            logger.info("Refreshing regions from " + REGIONS_URL);
            URL url = new URL(REGIONS_URL);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setConnectTimeout(5_000);
            conn.setReadTimeout(10_000);
            conn.setRequestProperty("Accept", "application/json");
            try (InputStream stream = conn.getInputStream();
                 Scanner scanner = new Scanner(stream, StandardCharsets.UTF_8.name())) {
                String raw = scanner.useDelimiter("\\A").next();
                JSONObject root = new JSONObject(raw);
                regionsCache = root.getJSONArray("regions");
                logger.info("regions.json refreshed from live URL (" + regionsCache.length() + " regions).");
                return regionsCache;
            }
        } catch (Exception e) {
            logger.warning("Live region refresh failed: " + e.getMessage());
            return null;
        }
    }

    private static JSONObject findRegion(JSONArray regions, String region) {
        String normalized = region.trim().toLowerCase().replace('_', '-');

        // Pass 1: match canonical id
        for (int i = 0; i < regions.length(); i++) {
            JSONObject row = regions.getJSONObject(i);
            if (row.getString("id").equals(normalized)) {
                return row;
            }
        }

        // Pass 2: match aliases (case-insensitive, _ == -)
        for (int i = 0; i < regions.length(); i++) {
            JSONObject row = regions.getJSONObject(i);
            JSONArray aliases = row.optJSONArray("alias");
            if (aliases == null) {
                continue;
            }
            for (int j = 0; j < aliases.length(); j++) {
                String alias = aliases.getString(j).toLowerCase().replace('_', '-');
                if (alias.equals(normalized)) {
                    return row;
                }
            }
        }

        throw new IllegalArgumentException("Invalid region: " + region);
    }

    private static String stripHttps(String url) {
        return url.replaceFirst("^https?://", "");
    }
}
