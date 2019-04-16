package com.structit.snake.service;

import android.os.AsyncTask;
import android.util.Log;
import android.webkit.CookieManager;

import com.structit.snake.utils.OnEventListener;

import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;
import java.util.logging.Level;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class LoginTask extends AsyncTask<URL, Void, Boolean> {

    private static final int MAX_ATTEMPT = 3;

    private Webservice mService;
    private Document mDocument;

    private Boolean isFirst = true;

    private OnEventListener mCallBack;

    public LoginTask(Webservice service, OnEventListener callback) {
        this.mService = service;
        this.mDocument = null;
        this.mCallBack = callback;
    }

    @Override
    protected Boolean doInBackground(URL... urls) {
        Boolean isConnected = false;
        int nbAttempt = 0;

        try {
            do {

                Log.d("webservice", "do");
                HttpURLConnection connection = (HttpURLConnection) urls[0].openConnection();

                connection.setRequestMethod("GET");
                connection.setDoOutput(true);
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(5000);

                String c = CookieManager.getInstance().getCookie("snake");
                Log.d("webservice", c);

                if(c!= null) {
                    this.isFirst = false;
                    connection.setRequestProperty("Cookie", c);
                }

                connection.connect();


                if (connection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                    Map<String, List<String>> headerFields = connection.getHeaderFields();
                    List<String> cookiesHeader = headerFields.get("Set-Cookie");

                    if (cookiesHeader != null) {
                        for (String cookie : cookiesHeader) {
                            CookieManager cookieManager = CookieManager.getInstance();
                            cookieManager.setCookie("snake", cookie);
                        }
                    }

                    BufferedReader reader = new BufferedReader(new InputStreamReader(
                            connection.getInputStream()));
                    StringBuffer buffer = new StringBuffer();
                    String inputLine;
                    while ((inputLine = reader.readLine()) != null) {
                        buffer.append(inputLine);
                    }
                    reader.close();

                    Log.d("webservice", "Buffer: " + buffer.toString());

                    DocumentBuilderFactory builderFactory = DocumentBuilderFactory
                            .newInstance();
                    DocumentBuilder documentBuilder = builderFactory.newDocumentBuilder();
                    InputSource inputSource = new InputSource(new StringReader(buffer.toString()));

                    this.mDocument = documentBuilder.parse(inputSource);

                    isConnected = true;
                }

                nbAttempt++;
            } while(!isConnected && nbAttempt < MAX_ATTEMPT);
        } catch(Exception ex) {
            Log.d("webservice", "catch");
            Logger logger = Logger.getAnonymousLogger();
            logger.log(Level.SEVERE, "an exception was thrown", ex);
        }

        return isConnected;
    }

    @Override
    protected void onPostExecute(final Boolean success) {
        if(success) {
            mCallBack.onResponse(mDocument);
        }

    }
}