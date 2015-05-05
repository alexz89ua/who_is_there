package com.stfalcon.whoisthere;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.facebook.Profile;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by root on 28.04.15.
 */
public class ProfileUser extends ActionBarActivity {
    String parserLink;
    SharedPreferences mSettings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        TextView name = (TextView) findViewById(R.id.name);
        name.setText("Name - " + FacebookActivity.user.name);

        TextView id = (TextView) findViewById(R.id.id);
        id.setText("Id - " + FacebookActivity.user.id);

        TextView email = (TextView) findViewById(R.id.email);
        email.setText("Email - " + FacebookActivity.user.email);

        TextView birthday = (TextView) findViewById(R.id.birthday);
        birthday.setText(FacebookActivity.user.birthday);

        TextView gender = (TextView) findViewById(R.id.gender);
        gender.setText("You - " + FacebookActivity.user.gender);

        ImageView im = (ImageView) findViewById(R.id.imageView);

        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(ProfileUser.this)
                .memoryCacheExtraOptions(100, 100) // width, height
                .discCacheExtraOptions(100, 100, Bitmap.CompressFormat.PNG, 100)
                .build();

        ImageLoader imageLoader = ImageLoader.getInstance();
        imageLoader.init(config);
        imageLoader.displayImage("http://graph.facebook.com/" + FacebookActivity.user.id + "/picture?type=large", im);



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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