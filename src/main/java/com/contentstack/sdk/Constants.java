package com.contentstack.sdk;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

public class Constants {

    public static final String SDK_VERSION = "1.8.0";
    public static final String ENVIRONMENT = "environment";
    public static final String CONTENT_TYPE_UID = "content_type_uid";


    // REQUEST  METHOD
    public enum REQUEST_METHOD {GET}

    // REQUEST_CONTROLLER
    public enum REQUEST_CONTROLLER {
        QUERY,
        ENTRY,
        ASSET,
        SYNC,
        CONTENTTYPES,
        ASSETLIBRARY
    }


    // GET REQUEST TYPE
    public static final String QUERYOBJECT = "getQueryEntries";
    public static final String SINGLEQUERYOBJECT = "getSingleQueryEntries";
    public static final String FETCHENTRY = "getEntry";
    public static final String FETCHALLASSETS = "getAllAssets";
    public static final String FETCHASSETS = "getAssets";
    public static final String FETCHSYNC = "getSync";
    public static final String FETCHCONTENTTYPES = "getContentTypes";


    // ERROR MESSAGE BLOCK
    public static final String JSON_NOT_PROPER = "Please provide valid JSON.";
    public static final String API_KEY_IS_NULL = "Stack api key can not be null.";
    public static final String CONTENT_TYPE_NAME = "Please set contentType name.";
    public static final String ACCESS_TOKEN_IS_NULL = "Access token can not be null.";
    public static final String ENVIRONMENT_IS_NULL = "Environment can not be null.";
    public static final String CALL_STACK = "You must called Contentstack.stack() first";
    public static final String QUERY_EXCEPTION = "Please provide valid params.";


    /**
     * Converts the given date to user&#39;s timezone.
     * @param date date in ISO format.
     * @param timeZone timezone selection
     * @return {@link Calendar} object.
     * @throws ParseException
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *   BuiltUtil.parseDate(dateString, TimeZone.getDefault());
     * </pre>
     *
     */
    public static Calendar parseDate(String date, TimeZone timeZone) throws ParseException {
        ArrayList<String> knownPatterns = new ArrayList<String>();
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

        for (String formatString : knownPatterns){
            try {
                return parseDate(date, formatString, timeZone);

            }catch (ParseException e) {}
        }

        return null;
    }

    /**
     * Converts the given date to the user&#39;s timezone.
     * @param date date in string format.
     * @param dateFormat date format.
     * @param timeZone TimeZone.
     * @return {@link Calendar} object.
     * @throws ParseException
     *
     * <br><br><b>Example :</b><br>
     * <pre class="prettyprint">
     *   BuiltUtil.parseDate(dateString, "yyyy-MM-dd'T'HH:mm:ssZ", TimeZone.getTimeZone("GMT"));
     * </pre>
     */

    public static Calendar parseDate(String date, String dateFormat, TimeZone timeZone) throws ParseException {
        Date dateObject   = null;
        String month      = "";
        String day        = "";
        String year       = "";
        String hourOfDay  = "";
        String min        = "";
        String sec        = "";
        Calendar cal      = Calendar.getInstance();

        SimpleDateFormat dateFormatter = new SimpleDateFormat(dateFormat);
        dateObject = dateFormatter.parse(date);

        month     = new SimpleDateFormat("MM").format(dateObject);
        day       = new SimpleDateFormat("dd").format(dateObject);
        year      = new SimpleDateFormat("yyyy").format(dateObject);
        hourOfDay = new SimpleDateFormat("HH").format(dateObject);
        min       = new SimpleDateFormat("mm").format(dateObject);
        sec       = new SimpleDateFormat("ss").format(dateObject);

        if(timeZone != null){
            cal.setTimeZone(timeZone);
        }else{
            cal.setTimeZone(TimeZone.getDefault());
        }

        cal.set(Integer.parseInt(year), Integer.parseInt(month)-1, Integer.parseInt(day), Integer.parseInt(hourOfDay), Integer.parseInt(min), Integer.parseInt(sec));

        month     = null;
        day       = null;
        year      = null;
        hourOfDay = null;
        min       = null;
        sec       = null;
        dateObject = null;

        return cal;
    }
}

