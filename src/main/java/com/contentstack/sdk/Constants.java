package com.contentstack.sdk;

import org.jetbrains.annotations.NotNull;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.logging.Logger;

/**
 * The type Constants for Internal Uses.
 *
 * @author Shailesh Mishra
 * @version 1.0.0
 * @since 01-11-2017
 */
public class Constants {

    private static final Logger logger = Logger.getLogger(Constants.class.getSimpleName());
    protected static final String SDK_VERSION = "1.10.2";
    protected static final String ENVIRONMENT = "environment";
    protected static final String CONTENT_TYPE_UID = "content_type_uid";
    protected static final String ENTRY_UID = "entry_uid";
    protected static final String LIVE_PREVIEW = "live_preview";

    protected static final String SYNCHRONISATION = "stacks/sync";
    // Errors
    protected static final String ERROR_CODE = "error_code";
    protected static final String ERROR_MESSAGE = "error_message";
    protected static final String ERRORS = "errors";
    // User-Agents
    protected static final String X_USER_AGENT_KEY = "X-User-Agent";
    protected static final String USER_AGENT_KEY = "User-Agent";
    protected static final String USER_AGENT = userAgent();
    protected static final String CONTENT_TYPE = "Content-Type";
    protected static final String APPLICATION_JSON = "application/json";

    protected static final String QUERY = "query";
    protected static final String EXCEPT = "except";
    protected static final String EXISTS = "$exists";
    protected static final String REGEX = "$regex";
    protected static final String LIMIT = "limit";
    protected static final String OPTIONS = "$options";

    protected Constants() {
        logger.warning("Not Allowed");
    }

    /**
     * The enum Request controller.
     */
    // REQUEST_CONTROLLER
    public enum REQUEST_CONTROLLER {
        QUERY, ENTRY, ASSET, SYNC, CONTENTTYPES, ASSETLIBRARY
    }

    // GET REQUEST TYPE
    public static final String QUERYOBJECT = "getQueryEntries";
    public static final String SINGLEQUERYOBJECT = "getSingleQueryEntries";
    public static final String FETCHENTRY = "getEntry";
    public static final String FETCHALLASSETS = "getAllAssets";
    public static final String FETCHASSETS = "getAssets";
    public static final String FETCHSYNC = "getSync";
    public static final String FETCHCONTENTTYPES = "getContentTypes";

    public static final String CONTENT_TYPE_NAME = "Please set contentType name.";
    public static final String QUERY_EXCEPTION = "Please provide valid params.";


    /**
     * @param dateString
     *         the date in string format
     * @param zoneId
     *         the string zoneId
     * @return Calendar
     */
    public static Calendar parseDateToTimeZone(@NotNull String dateString, @NotNull String zoneId) {
        //String dateString = "2016-12-16T12:36:33.961Z";
        Instant instant = Instant.parse(dateString);
        // Define the target time zone
        ZoneId targetTimeZone = ZoneId.of(zoneId);
        // Convert the instant to the target time zone
        ZonedDateTime dateTime = instant.atZone(targetTimeZone);
        // Extract the year, month, day, hour, minute, and second
        Calendar cal = Calendar.getInstance();
        cal.set(dateTime.getYear(), dateTime.getMonthValue(), dateTime.getDayOfMonth(), dateTime.getHour(), dateTime.getMinute(), dateTime.getSecond());
        return cal;
    }

    private static Calendar toCalendar(@NotNull String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
        LocalDateTime dt = LocalDateTime.parse(date, formatter);
        Calendar cal = Calendar.getInstance();
        cal.set(dt.getYear(), dt.getMonthValue(), dt.getDayOfMonth(), dt.getHour(), dt.getMinute(), dt.getSecond());
        return cal;
    }


    /**
     * @param date
     *         The date in string format like (String dateString = "2016-12-16T12:36:33.961Z";)
     * @param timeZone
     *         the time zone as string
     * @return calendar @{@link Calendar}
     */
    public static Calendar parseDate(@NotNull String date, TimeZone timeZone) {
        // Use the ISO-8601 format to parse the date string
        if (date.isEmpty()){
            return null;
        }
        Calendar cal = toCalendar(date);
        if (timeZone != null) {
            cal.setTimeZone(timeZone);
        } else {
            cal.setTimeZone(TimeZone.getDefault());
        }
        return cal;
    }

    protected static String userAgent() {
        String agent = System.getProperty("http.agent");
        String agentStr = agent != null ? agent : ("Java" + System.getProperty("java.version"));
        agentStr = agentStr + "/" + SDK_VERSION;
        return agentStr;
    }

}
