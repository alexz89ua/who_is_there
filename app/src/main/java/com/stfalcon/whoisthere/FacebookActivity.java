package com.stfalcon.whoisthere;

import android.content.Intent;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookSdk;
import com.facebook.AccessToken;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.Profile;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.FacebookException;
import com.facebook.login.widget.ProfilePictureView;

import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;
import org.json.JSONException;



public class FacebookActivity extends ActionBarActivity {

    LoginButton loginButton; TextView nameT,idT,linkT;
    ProfilePictureView profilePictureView;

    CallbackManager callbackManager;

    @Override
    public void onActivityResult(int requestCode, int responseCode, Intent data)
    {
        super.onActivityResult(requestCode, responseCode, data);
        callbackManager.onActivityResult(requestCode, responseCode, data);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();

        setContentView(R.layout.activity_facebook);

        loginButton = (LoginButton)this.findViewById(R.id.login_button);
        nameT= (TextView)this.findViewById(R.id.textView);
        linkT= (TextView)this.findViewById(R.id.textViewEmail);
        idT= (TextView)this.findViewById(R.id.textViewId);

        if(AccessToken.getCurrentAccessToken()!=null)
        {
            nameT.setText(Profile.getCurrentProfile().getName());
            idT.setText(Profile.getCurrentProfile().getId());
            profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
            profilePictureView.setProfileId(Profile.getCurrentProfile().getId());
            linkT.setText(Profile.getCurrentProfile().getLinkUri().toString());
        } else
        {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "You must Login", Toast.LENGTH_SHORT);
            toast.show();
        }
       /* List<String> permissionNeeds = Arrays.asList("user_photos", "email", "user_birthday", "user_location", "public_profile");

        loginButton.setReadPermissions(permissionNeeds);*/

        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {

            @Override
            public void onSuccess(LoginResult loginResult) {
                GraphRequest request = GraphRequest.newMeRequest
                        (loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {
                                try {
                                    String id = object.getString("id");
                                    String name = object.getString("name");
                                    String link = object.getString("link");

                                    User user = new User(name,id,link);

                                    nameT.setText(user.getName());
                                    idT.setText(user.getId());

                                    profilePictureView = (ProfilePictureView) findViewById(R.id.friendProfilePicture);
                                    profilePictureView.setProfileId(user.getId());

                                    //String url = "http://graph.facebook.com/839669416127599/picture";

                                    linkT.setText(user.getLink());

                                } catch (JSONException e) {
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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_facebook, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


}
