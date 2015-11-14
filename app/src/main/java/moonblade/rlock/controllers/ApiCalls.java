package moonblade.rlock.controllers;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

/**
 * Created by moonblade on 7/27/15.
 */
public class ApiCalls extends AsyncTask< Void,Void,StringBuffer> {
    StringBuffer serverOut;
    Map<String,Object> params;
    Context context;
    URL url;
    public AsyncResponse listener = null;

    public ApiCalls(Context context,Map<String,Object> params, String url, AsyncResponse listener){
        this.params=params;
        this.context = context;
        this.listener=listener;
        try {
            this.url= new URL(url);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected StringBuffer doInBackground(Void... voids) {
        callApi(params);
        return serverOut;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
    }

    @Override
    protected void onPostExecute(StringBuffer s) {
        listener.ProcessFinish(s);
    }

    private void callApi(Map<String, Object> params) {
        serverOut = new StringBuffer("");
        try {
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, Object> param : params.entrySet()) {
                if (postData.length() != 0) postData.append('&');
                postData.append(URLEncoder.encode(param.getKey(), "UTF-8"));
                postData.append('=');
                Log.i("id", param.getKey());
                Log.i("val", String.valueOf(param.getValue()));
                postData.append(URLEncoder.encode(String.valueOf(param.getValue()), "UTF-8"));
            }
            byte[] postDataBytes = postData.toString().getBytes("UTF-8");

            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            connection.setRequestProperty("Content-Length", String.valueOf(postDataBytes.length));
            connection.setRequestMethod("POST");
            connection.setDoOutput(true);
            connection.getOutputStream().write(postDataBytes);
            connection.connect();

            InputStream inputStream = connection.getInputStream();

            BufferedReader rd = new BufferedReader(new InputStreamReader(inputStream));
            String line = "";
            while ((line = rd.readLine()) != null) {
                serverOut.append(line);
            }
            Log.i("the server response was", String.valueOf(serverOut));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
