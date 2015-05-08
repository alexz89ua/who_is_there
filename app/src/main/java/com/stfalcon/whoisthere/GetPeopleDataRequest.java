package com.stfalcon.whoisthere;

import com.octo.android.robospice.request.SpiceRequest;

import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.CharEncoding;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by root on 06.05.15.
 */
public class GetPeopleDataRequest extends SpiceRequest<Wrapper> {

    String url;

    public GetPeopleDataRequest(String url) {
        super(Wrapper.class);
        this.url = url;
    }

    @Override
    public Wrapper loadDataFromNetwork() throws Exception {

        Wrapper wrapper = new Wrapper();
        wrapper.code=Code.GET_USER_DATA_OK;
        wrapper.obj=Parser(IOUtils.toString(new InputStreamReader(new URL(url).openStream(), CharEncoding.UTF_8)));

return wrapper;
    }


    private ArrayList<People> Parser(String json_array) {
        ArrayList<People> arr = new ArrayList<People>();
        String r = json_array.replace("[", "{ \"Users\" : [");
        String rr = r.replace("]", "] }");


        JSONObject json_obj = null;
        try {
            json_obj = new JSONObject(rr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        JSONArray jsonMainNode = json_obj.optJSONArray("Users");
            int lengthJsonArr = jsonMainNode.length();

            for (int i = 0; i < lengthJsonArr; i++) {
                JSONObject jsonChildNode = null;
                try {
                    jsonChildNode = jsonMainNode.getJSONObject(i);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                String name = jsonChildNode.optString("name").toString();
                String id = jsonChildNode.optString("id").toString();
                String x = jsonChildNode.optString("x").toString();
                String y = jsonChildNode.optString("y").toString();

                arr.add(new People(id,name,x,y));


        }
        return arr;
    }
}
