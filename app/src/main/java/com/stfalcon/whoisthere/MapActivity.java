package com.stfalcon.whoisthere;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class MapActivity extends BaseSpiceActivity
        implements LocationListener, GoogleMap.OnMarkerClickListener {
    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */

    String TITLES[] = {"Profile", "Settings", "Exit"};
    int ICONS[] = {R.drawable.profile, R.drawable.settings, R.drawable.exit};


    String NAME;
    String ID;
    String PASS;

    RecyclerView mRecyclerView;
    RecyclerView.Adapter mAdapter;
    RecyclerView.LayoutManager mLayoutManager;
    DrawerLayout Drawer;
    ActionBarDrawerToggle mDrawerToggle;
    SupportMapFragment mapFragment;
    GoogleMap myMap;
    GetPeopleDataRequest RequestGetUser = new GetPeopleDataRequest("https://who-is-there.herokuapp.com/");
    ;
    SendUserDaraRequest sendUserDaraRequest;
    List<String> name_arr;
    int zoom = 4000;
    TextRequestListener textRequestListener = new TextRequestListener();
    private Toolbar toolbar;
    private LocationManager locationManager;
    private Marker myMarker;
    private PopupWindow pwindo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        FacebookActivity.fa.finish();

        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        NAME = FacebookActivity.user.name;
       /* Toast toast = Toast.makeText(getApplicationContext(),
                NAME, Toast.LENGTH_SHORT);
        toast.show();*/
        ID = FacebookActivity.user.id;
        PASS = "http://graph.facebook.com/" + ID + "/picture?type=large";

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView);

        mRecyclerView.setHasFixedSize(true);

        ImageView im = (ImageView) findViewById(R.id.imageView);

        mAdapter = new MyAdapter(TITLES, ICONS, NAME, ID, PASS, im, this);

        mRecyclerView.setAdapter(mAdapter);                              // Setting the adapter to RecyclerView

        mLayoutManager = new LinearLayoutManager(this);                 // Creating a layout Manager

        mRecyclerView.setLayoutManager(mLayoutManager);                 // Setting the layout Manager


        Drawer = (DrawerLayout) findViewById(R.id.DrawerLayout);        // Drawer object Assigned to the view

        mDrawerToggle = new ActionBarDrawerToggle(this, Drawer, toolbar, R.string.drawer_open, R.string.drawer_close) {


            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                // code here will execute once the drawer is opened( As I dont want anything happened whe drawer is
                // open I am not going to put anything here)
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                // Code here will execute once drawer is closed
            }

        }; // Drawer Toggle Object Made
        Drawer.setDrawerListener(mDrawerToggle); // Drawer Listener set to the Drawer toggle
        mDrawerToggle.syncState();


        MyLocationListener.SetUpLocationListener(this);

        InitMap();
        InitPrimeUser();

    }

    protected void onStart() {
        super.onStart();
        getSpiceManager().execute(RequestGetUser, textRequestListener);
    }

    private void InitMap() {
        mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        myMap = mapFragment.getMap();
        myMap.setOnMarkerClickListener((GoogleMap.OnMarkerClickListener) this);
        if (myMap == null) {
            finish();
        }
    }

    private void InitPrimeUser() {
        myMap.setMyLocationEnabled(true);
        /*myMap.clear(); поки що немає необхідності*/
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Location myLoc = MyLocationListener.imHere;
        if (myLoc != null) {
            double longitude = myLoc.getLongitude();
            double latitude = myLoc.getLatitude();
            /*Toast toast = Toast.makeText(getApplicationContext(),
                    "Вас знайдено!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();*/
            myMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), 15.5f), zoom, null);
            myMarker = myMap.addMarker(new MarkerOptions()
                    .position(new LatLng(latitude, longitude))
                    .title("Your position")
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));
        } else {
            Toast toast = Toast.makeText(getApplicationContext(),
                    "Ваше місцеположення не знайдено!",
                    Toast.LENGTH_SHORT);
            toast.setGravity(Gravity.CENTER, 0, 0);
            toast.show();

        }

    }

    public void showInfoWindow() {
        LayoutInflater inflater = (LayoutInflater) MapActivity.this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.info_window,
                (ViewGroup) findViewById(R.id.popup));
        pwindo = new PopupWindow(layout, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT, true);
        pwindo.setBackgroundDrawable(new ColorDrawable());
        pwindo.showAtLocation(layout, Gravity.CENTER, 0, 0);
    }

    public void ProfileBtnClick(View layout) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "ProfileBtn_Pressed",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void ChatBtnClick(View layout) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "ChatBtn_Pressed",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public void AddBtnClick(View layout) {
        Toast toast = Toast.makeText(getApplicationContext(),
                "AddBtn_Pressed",
                Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    @Override
    public boolean onMarkerClick(final Marker marker) {
        if (marker.equals(myMarker)) {

            showInfoWindow();

        }
        return true;
    }

    public void onSectionAttached(int number) {
        /*switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    @Override
    public void onLocationChanged(Location loc) {
        /*InitPrimeUser();*/
        FacebookActivity.user.x = loc.getLatitude();
        FacebookActivity.user.y = loc.getLongitude();
        String url = "https://who-is-there.herokuapp.com/hello/" + FacebookActivity.user.id + "/" +
                FacebookActivity.user.name + "/" + FacebookActivity.user.x + "/" + FacebookActivity.user.y;
        try {
            URL u = new URL(url);
            sendUserDaraRequest = new SendUserDaraRequest(u);
            getSpiceManager().execute(sendUserDaraRequest, textRequestListener);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getSpiceManager().execute(RequestGetUser, textRequestListener);

    }

    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void CloseApp() {
        this.finish();
    }

    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        public PlaceholderFragment() {
        }

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_map_activity, container, false);
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MapActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

    public final class TextRequestListener implements RequestListener<Wrapper> {

        @Override
        public void onRequestFailure(SpiceException spiceException) {
            Toast.makeText(MapActivity.this, "failure", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onRequestSuccess(final Wrapper result) {
            switch (result.code) {
                case Code.GET_USER_DATA_OK:
                    ArrayList<People> arr_p = result.obj;
                    People p = arr_p.get(1);
                    Toast toast = Toast.makeText(getApplicationContext(), p.name, Toast.LENGTH_SHORT);
                    toast.show();
                    break;
                case Code.SEND_USER_DATA_OK:
                    Toast.makeText(MapActivity.this, "yeah", Toast.LENGTH_SHORT).show();
                    break;
            }


           /* Toast toast = Toast.makeText(getApplicationContext(), name_arr.get(1), Toast.LENGTH_SHORT);
            toast.show();*/
        }
    }

}
