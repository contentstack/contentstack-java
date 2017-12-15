package com.builtio.contentstack;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;


public class URLConnection {

    private static final String TAG = "URLConnection";

    public void getDataRequest(String csUrl, String requestMethod, String postParam) throws IOException {

        URL obj = new URL(csUrl);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod(requestMethod);
        //con.setRequestProperty("User-Agent", USER_AGENT);
        int responseCode = con.getResponseCode();
        System.out.println("GET Response Code :: " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK) { // success
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            // print result
            System.out.println(response.toString());
        } else {
            System.out.println("GET request not worked");
        }

    }










    public void postDataRequest(String contentstackUrl, String requestMethod, Map<String,Object> params) throws IOException {

        String requesturl = contentstackUrl+"?"+postParamData(params).toString();

        URL obj = new URL(contentstackUrl+"?");
        HttpURLConnection connection = (HttpURLConnection) obj.openConnection();
        connection.setRequestMethod("GET");
        //connection.setRequestProperty("User-Agent", USER_AGENT);
        connection.setDoOutput(true);


        OutputStream outputStream = connection.getOutputStream();

        System.out.println("CS Url : "+requesturl);
        System.out.println("Request method : "+requestMethod);
        System.out.println("Post params : "+postParamData(params).toString());

        if (!postParamData(params).isEmpty() && postParamData(params)!=null){
            //replace("%2F","/")
            outputStream.write(postParamData(params).toString().getBytes("UTF-8"));
        }


        //System.out.println("OutputStream "+outputStream);
        outputStream.flush();
        outputStream.close();
        int responseCode = connection.getResponseCode();
        System.out.println("POST Response Code :: " + responseCode);

        if (responseCode == HttpURLConnection.HTTP_OK) { //success
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

        } else if (responseCode == HttpURLConnection.HTTP_UNAUTHORIZED)
        {
            this.sendError(HttpURLConnection.HTTP_UNAUTHORIZED, "UNAUTHORIZED REQUEST");
        }
        else
        {
            this.sendError(0, "POST request not worked");
        }
    }











    private void sendError(int responseCode, String respMessage){
        System.out.print(responseCode+":: "+respMessage);
    }

    public String postParamData(Map<String,Object> params){

        String postDataString = "";
        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String,Object> paramEntry : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(paramEntry.getKey(), "UTF-8"));
                postData.append('=');
                postData.append(URLEncoder.encode(String.valueOf(paramEntry.getValue()), "UTF-8"));
            }
            postDataString = postData.toString();
        } catch (Exception e) {
            postDataString = "";
            System.out.println(TAG+": "+ e.toString());
        }

        return postDataString;
    }







    public  void sendGET(String GET_URL, String requestMethod, String params) throws IOException
    {

        System.out.println("url: "+GET_URL);
        System.out.println("requestMethod: "+requestMethod);
        System.out.println("params: "+params);

        URL objURL = new URL(GET_URL);
        HttpURLConnection con = (HttpURLConnection) objURL.openConnection();
        con.setRequestMethod(requestMethod);

        con.setRequestProperty("Content-Type", "application/json");
        con.setRequestProperty("api_key", "blt920bb7e90248f607");
        con.setRequestProperty("access_token", "blt0c4300391e033d4a59eb2857");
        con.setRequestProperty("environment", "production");

        con.setDoOutput(true);
        con.setDoInput(true);

        OutputStream outputStream = con.getOutputStream();
        String paramString = params.toString().trim();

        if (!paramString.isEmpty() && paramString!=null)
        {
            byte[] postParams = paramString.getBytes("UTF-8");
            outputStream.write(postParams);
        }

        outputStream.flush();
        outputStream.close();
        int responseCode = con.getResponseCode();

        System.out.println("GET Response Code : " + responseCode);
        if (responseCode == HttpURLConnection.HTTP_OK)
        {
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            String inputLine;
            StringBuffer response = new StringBuffer();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();
            System.out.println(response.toString());

        } else {
            System.out.println("GET request not worked");
        }

    }

}
