package com.stfalcon.whoisthere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.gson.Gson;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.simple.SimpleTextRequest;

import org.json.JSONObject;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;


public class FacebookActivity extends BaseSpiceActivity {

    LoginButton loginButton;
    TextView TextView_Name, TextView_Id, TextView_Link;

    private SharedPreferences mSettings;
    CallbackManager callbackManager;

    static User user;
    HashMap<String, String> data;

    private SimpleTextRequest txtRequest, loadUserInformation;
    private SendUserDaraRequest sendUserDaraRequest;
    String url;
    URL u;
    public static Activity fa;

    TextRequestListener textRequestListener;

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data) {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        InitializationFacebook(savedInstanceState);

        InitializationView();

        CheckUserLogin();

      //  textRequestListener = new TextRequestListener();

        LoginButtonCLick();

      //  sendUserDaraRequest = new SendUserDaraRequest(u);
       /* List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "user_location", "public_profile");

        loginButton.setReadPermissions(permissionNeeds);*/

    }

    @Override
    protected void onStart() {
        super.onStart();

        /*getSpiceManager().execute(txtRequest, "txt", DurationInMillis.ONE_MINUTE,
                new TextRequestListener());*/

        //getSpiceManager().execute(sendUserDaraRequest, textRequestListener);
    }

    public final class TextRequestListener implements RequestListener<String> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(FacebookActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final String result) {

            Toast.makeText(FacebookActivity.this, "success", Toast.LENGTH_SHORT).show();


        }
    }


    public void InitializationFacebook(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_facebook);

        fa = this;
    }

    public void InitializationView() {
        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        TextView_Name = (TextView) this.findViewById(R.id.textView);
        TextView_Link = (TextView) this.findViewById(R.id.textViewEmail);
        TextView_Id = (TextView) this.findViewById(R.id.textViewId);
    }

    public void CheckUserLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {


           /* Toast toast = Toast.makeText(getApplicationContext(),
                    AccessToken.getCurrentAccessToken().toString(), Toast.LENGTH_SHORT);
            toast.show();*/

            mSettings = this.getSharedPreferences("ka", Context.MODE_PRIVATE);

            String s = mSettings.getString("parser", "nooooo");

            user = new Gson().fromJson(s, User.class);
            user.name = user.name.replace(" ", "_");
            /*
            mSettings = this.getSharedPreferences("ka", Context.MODE_PRIVATE);
            String s = mSettings.getString("parser", "nooooo");*/


            url = "https://who-is-there.herokuapp.com/hello/" + user.id + "/" + user.name + "/228/48";
            Log.v("URL", url);
            try {
                u = new URL(url);
                sendUserDaraRequest = new SendUserDaraRequest(u);
                getSpiceManager().execute(sendUserDaraRequest, textRequestListener);
                Intent intent = new Intent(FacebookActivity.this, MapActivity.class);
                startActivity(intent);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            /*TextView_Id.setText(user.id);
            TextView_Link.setText(user.link);*/

        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You must Login", Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    public void LoginButtonCLick() {
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {


                final GraphRequest request = GraphRequest.newMeRequest
                        (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {

                                    user = new Gson().fromJson(object.toString(), User.class);
                                    user.name = user.name.replace(" ", "_");

                                   /* TextView_Name.setText(user.name);
                                    TextView_Id.setText(user.id);
                                    ProfileFoto(user.Get_Pass_To_Profile_Foto());
                                    TextView_Link.setText(user.link);*/

                                    //String url = "http://graph.facebook.com/839669416127599/picture";

                                    //URL for parse data
                                    Log.v("URL", response.getConnection().getURL().toString());

                                    //SharedPref(response.getConnection().getURL().toString());


                                    mSettings = getSharedPreferences("ka", Context.MODE_PRIVATE);
                                    SharedPreferences.Editor editor = mSettings.edit();
                                    editor.putString("parser", object.toString());
                                    editor.commit();


                                   /* Intent intent = new Intent(FacebookActivity.this, MapActivity.class);
                                    intent.putExtra("name", user.name);
                                    intent.putExtra("id", user.id);
                                    intent.putExtra("pass", "http://graph.facebook.com/" + user.id + "/picture?type=large");
                                    intent.putExtra("parseLink", response.getConnection().getURL().toString());*/
                                    //startActivity(intent);
                                    //finish();

                                    url = "https://who-is-there.herokuapp.com/hello/" + user.id + "/"+user.name+"/228/48";

                                    TextView_Id.setText(user.id);
                                    TextView_Link.setText(url);

                                    try {
                                        u = new URL(url);
                                        sendUserDaraRequest = new SendUserDaraRequest(u);
                                        getSpiceManager().execute(sendUserDaraRequest, textRequestListener);
                                    } catch (MalformedURLException e) {
                                        e.printStackTrace();
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();

                                }

                            }
                        });
                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,email,gender, birthday,link");
                request.setParameters(parameters);
                request.executeAsync();

            }

            @Override
            public void onCancel() {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "onCancel", Toast.LENGTH_SHORT);
                toast.show();
            }

            @Override
            public void onError(FacebookException exception) {
                Toast toast = Toast.makeText(getApplicationContext(),
                        "onError", Toast.LENGTH_SHORT);
                toast.show();
                Log.v("LoginActivity", exception.getCause().toString());
            }
        });
    }



}
