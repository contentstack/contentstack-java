package com.contentstack.sdk.utility;

import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import java.io.*;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;

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

public class CSAppUtils {


    public CSAppUtils(){}

    /**
     * To check if required response within given time window available in cache
     * @param file cache file.
     * @param time time
     * @return true if cache data available which satisfy given time condition.
     */

    public boolean getResponseTimeFromCacheFile(File file, long time) {
        try{
            JSONObject jsonObj = getJsonFromCacheFile(file);
            long responseDate =  Long.parseLong(jsonObj.optString("timestamp"));

            Calendar responseCalendar = Calendar.getInstance();

            responseCalendar.add(Calendar.MINUTE, 0);
            responseCalendar.set(Calendar.SECOND, 0);
            responseCalendar.set(Calendar.MILLISECOND, 0);
            responseCalendar.setTimeInMillis(responseDate);
            responseCalendar.getTimeInMillis();


            Calendar currentCalendar = Calendar.getInstance();
            currentCalendar.setTime(new Date());
            currentCalendar.getTimeInMillis();

            long dateDiff = (currentCalendar.getTimeInMillis() - responseCalendar.getTimeInMillis());
            long dateDiffInMin = dateDiff / (60 * 1000);


            if(dateDiffInMin > (time / 60000)){
                return true;// need to send call.
            }else{
                return false;// no need to send call.
            }

        }catch(Exception e) {
            e.printStackTrace();
            return false;
        }
    }


    /**
     * To retrieve data from cache.
     *
     * @param file cache file.
     * @return cache data in JSON.
     */
    public static JSONObject getJsonFromCacheFile(File file) {

        JSONObject json              = null;
        InputStream input            = null;
        ByteArrayOutputStream buffer = null;
        try{

            input = new BufferedInputStream(new FileInputStream(file));
            buffer = new ByteArrayOutputStream();
            byte[] temp = new byte[1024];
            int read;
            while((read = input.read(temp)) > 0){
                buffer.write(temp, 0, read);
            }
            json = new JSONObject(buffer.toString("UTF-8"));
            buffer.flush();
            buffer.close();
            input.close();
            return json;

        }catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * To encrypt given value.
     * @param value string
     * @return MD5 value
     *
     */

    public String getMD5FromString(String value) {
        String output;
        output = value.toString().trim();
        if(value.length() > 0){
            try {
                // Create MD5 Hash
                MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
                digest.reset();
                digest.update(output.getBytes());
                byte messageDigest[] = digest.digest();

                // Create Hex String
                StringBuffer hexString = new StringBuffer();
                for (int i = 0; i < messageDigest.length; i++) {

                    String hex = Integer.toHexString(0xFF & messageDigest[i]);
                    if(hex.length() == 1){
                        hexString.append('0');
                    }
                    hexString.append(hex);
                }

                return hexString.toString();

            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }
        }else{
            return null;
        }
    }



    /**
     * Converts the given date to the user&#39;s timezone.
     * @param date {@link String}
     * @param dateFormat {@link String}
     * @param timeZone String
     * @return Calendar
     * @throws ParseException
     *
     * <br><br><b>Example :</b><br>
     *      <pre class="prettyprint">
     *        CSAppUtils.parseDate(dateString, "yyyy-MM-dd'T'HH:mm:ssZ", TimeZone.getTimeZone("GMT"));
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

        cal.set(Integer.valueOf(year), Integer.valueOf(month)-1, Integer.valueOf(day), Integer.valueOf(hourOfDay), Integer.valueOf(min), Integer.valueOf(sec));

        month     = null;
        day       = null;
        year      = null;
        hourOfDay = null;
        min       = null;
        sec       = null;
        dateObject = null;

        return cal;
    }


    /**
     * Type to compare dates.
     * @author Contentstack
     */
    public static enum DateComapareType{
        WEEK, DAY, HOURS, MINUTES, SECONDS
    };
}