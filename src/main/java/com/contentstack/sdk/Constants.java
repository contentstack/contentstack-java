package com.contentstack.sdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The type Constants.
 */
public class Constants {

    private static final Logger logger = Logger.getLogger(Constants.class.getSimpleName());
    protected static final String SDK_VERSION = "1.10.0";
    protected static final String ENVIRONMENT = "environment";
    protected static final String CONTENT_TYPE_UID = "content_type_uid";
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
     * Parse date calendar.
     *
     * @param date
     *         the date
     * @param timeZone
     *         the time zone
     * @return the calendar
     */
    public static Calendar parseDate(String date, TimeZone timeZone) {
        ArrayList<String> knownPatterns = new ArrayList<>();
        knownPatterns.add("yyyy-MM-dd'T'HH:mm:ssZ");
        knownPatterns.add("yyyy-MM-dd'T'HH:mm:ss'Z'");
        knownPatterns.add("yyyy-MM-dd'T'HH:mm.ss'Z'");
        knownPatterns.add("yyyy-MM-dd'T'HH:mmZ");
        knownPatterns.add("yyyy-MM-dd'T'HH:mm'Z'");
        knownPatterns.add("yyyy-MM-dd'T'HH:mm'Z'");
        knownPatterns.add("yyyy-MM-dd'T'HH:mm:ss");
        knownPatterns.add("yyyy-MM-dd' 'HH:mm:ss");
        knownPatterns.add("yyyy-MM-dd");
        knownPatterns.add("HH:mm:ssZ");
        knownPatterns.add("HH:mm:ss'Z'");

        for (String formatString : knownPatterns) {
            try {
                return parseDate(date, formatString, timeZone);
            } catch (ParseException e) {
                logger.log(Level.WARNING, e.getLocalizedMessage(), e);
            }
        }
        return null;
    }

    /**
     * Parse date calendar.
     *
     * @param date
     *         the date
     * @param dateFormat
     *         the date format
     * @param timeZone
     *         the time zone
     * @return the calendar
     * @throws ParseException
     *         the parse exception
     */
    public static Calendar parseDate(String date, String dateFormat, TimeZone timeZone) throws ParseException {
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        Date dateObject = dateFormatter.parse(date);
        String month = new SimpleDateFormat("MM").format(dateObject);
        String day = new SimpleDateFormat("dd").format(dateObject);
        String year = new SimpleDateFormat("yyyy").format(dateObject);
        String hourOfDay = new SimpleDateFormat("HH").format(dateObject);
        String min = new SimpleDateFormat("mm").format(dateObject);
        String sec = new SimpleDateFormat("ss").format(dateObject);

        if (timeZone != null) {
            cal.setTimeZone(timeZone);
        } else {
            cal.setTimeZone(TimeZone.getDefault());
        }
        cal.set(Integer.parseInt(year), Integer.parseInt(month) - 1, Integer.parseInt(day), Integer.parseInt(hourOfDay),
                Integer.parseInt(min), Integer.parseInt(sec));
        return cal;
    }

    protected static String userAgent() {
        String agent = System.getProperty("http.agent");
        String agentStr = agent != null ? agent : ("Java" + System.getProperty("java.version"));
        agentStr = agentStr + "/" + SDK_VERSION;
        return agentStr;
    }

}
