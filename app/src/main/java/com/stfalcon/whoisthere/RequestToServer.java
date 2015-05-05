package com.stfalcon.whoisthere;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.widget.Toast;

import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.SimpleTextRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class RequestToServer extends Service {

    SimpleTextRequest txtRequest;

    public RequestToServer() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        txtRequest = new SimpleTextRequest("http://androiddocs.ru/api/friends.json");

        BaseSpiceActivity b = new BaseSpiceActivity() {
            @Override
            protected void onStart() {
                super.onStart();
                getSpiceManager().execute(txtRequest, "txt", DurationInMillis.ONE_MINUTE,
                        new TextRequestListener());

            }

            @Override
            protected void onStop() {
                super.onStop();
            }

            @Override
            protected SpiceManager getSpiceManager() {
                return super.getSpiceManager();
            }
        };
    }

    @Override
    public void onStart(Intent intent, int startid) {
        Toast.makeText(this, "Служба запущена",
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDestroy() {
        Toast.makeText(this, "Служба остановлена",
                Toast.LENGTH_SHORT).show();
    }

    public void Work_With_Json_Array(String json_array) {
        try {

            JSONObject json_obj = new JSONObject(json_array);


           /* name.setText("Name - " + json_obj.getString("name"));

            id.setText("Id - " + json_obj.getString("id"));

            gender.setText("You - " + json_obj.getString("gender"));*/

        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }

    public final class TextRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(RequestToServer.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final String result) {

            Toast.makeText(RequestToServer.this, "success", Toast.LENGTH_SHORT).show();

            Work_With_Json_Array(result);
        }
    }
}
