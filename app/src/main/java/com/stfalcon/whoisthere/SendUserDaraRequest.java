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
public class SendUserDaraRequest extends SpiceRequest<Wrapper> {

    URL url;

    public SendUserDaraRequest(URL url) {
        super(Wrapper.class);
        this.url = url;
    }

    @Override
    public Wrapper loadDataFromNetwork() throws Exception {
        Wrapper wrapper = new Wrapper();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            if (urlConnection.getResponseCode() == urlConnection.HTTP_OK) {
                wrapper.code=Code.SEND_USER_DATA_OK;
            } else {
                wrapper.code=Code.SEND_USER_DATA_FALSE;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }


        return wrapper;
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
