package com.stfalcon.whoisthere;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.google.gson.Gson;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONObject;

public class FacebookActivity extends ActionBarActivity {

    LoginButton loginButton;
    TextView TextView_Name, TextView_Id, TextView_Link;
    ProfilePictureView profilePictureView;
    public static final String APP_PREFERENCES = "mysettings";
    public static final String APP_PREFERENCES_NAME = "pass"; // имя кота

    private SharedPreferences mSettings;
    CallbackManager callbackManager;

    static User user;

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

        LoginButtonCLick();

/* List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "user_location", "public_profile");

loginButton.setReadPermissions(permissionNeeds);*/

    }

    public void InitializationFacebook(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_facebook);
    }

    public void InitializationView() {
        loginButton = (LoginButton) this.findViewById(R.id.login_button);
        TextView_Name = (TextView) this.findViewById(R.id.textView);
        TextView_Link = (TextView) this.findViewById(R.id.textViewEmail);
        TextView_Id = (TextView) this.findViewById(R.id.textViewId);
    }

    public void CheckUserLogin() {
        if (AccessToken.getCurrentAccessToken() != null) {
            mSettings = this.getSharedPreferences("ka", Context.MODE_PRIVATE);

            String s = mSettings.getString("parser", "nooooo");
            user = new Gson().fromJson(s, User.class);
/*TextView_Name.setText(user.name);
TextView_Id.setText(user.id);
TextView_Link.setText(user.link);
ProfileFoto(user.Get_Pass_To_Profile_Foto());*/
/*mSettings = this.getSharedPreferences("ka", Context.MODE_PRIVATE);

String s = mSettings.getString("parser", "nooooo");*/

            Intent intent = new Intent(FacebookActivity.this, MapActivity.class);
            intent.putExtra("name", user.name);
            intent.putExtra("id", user.id);
            intent.putExtra("parser", user.id);
            intent.putExtra("pass", "http://graph.facebook.com/" + user.id + "/picture?type=large");
            startActivity(intent);
            this.finish();
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
                                    user = new Gson().fromJson(object.toString(),
                                            User.class);
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

                                    Intent intent = new Intent(FacebookActivity.this, MapActivity.class);
                                    intent.putExtra("name", user.name);
                                    intent.putExtra("id", user.id);
                                    intent.putExtra("pass", "http://graph.facebook.com/" + user.id + "/picture?type=large");
                                    intent.putExtra("parseLink", response.getConnection().getURL().toString());
                                    startActivity(intent);
                                    finish();

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

    public void ProfileFoto(String pass) {
        ImageView im = (ImageView) findViewById(R.id.imageView);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(FacebookActivity.this)
                .memoryCacheExtraOptions(100, 100) // width, height
                .discCacheExtraOptions(100, 100, Bitmap.CompressFormat.PNG, 100)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage(pass, im);
    }

    public void SharedPref(String pass) {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(this);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("pass", pass);
        editor.putInt("int", 228);
        editor.apply();
    }

}