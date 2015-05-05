package com.stfalcon.whoisthere;

import com.octo.android.robospice.request.SpiceRequest;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by root on 30.04.15.
 */
public class SendUserDaraRequest extends SpiceRequest<String> {

    URL url;

    public SendUserDaraRequest(URL url) {
        super(String.class);
        this.url = url;
    }

    @Override
    public String loadDataFromNetwork() throws Exception {

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                return "success";
            } else {
                return String.valueOf(urlConnection.getResponseCode()) + " " + urlConnection.getResponseMessage();
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        return null;
    }

    private String readInputsream(InputStream in) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        StringBuilder result = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            result.append(line);
        }
        return line;
    }
}
