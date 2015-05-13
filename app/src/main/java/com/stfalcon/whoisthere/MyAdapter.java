package com.stfalcon.whoisthere;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

/**
 * Created by hp1 on 28-12-2014.
 */
public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public String parse;
    Context context;
    private String mNavTitles[];
    private int mIcons[];
    private String name;
    private String id;
    private String pass;
    private ImageView im;


    MyAdapter(String Titles[], int Icons[], String Name, String Email,String Pass, ImageView imm, Context passedContext) { // MyAdapter Constructor with titles and icons parameter

        mNavTitles = Titles;                //have seen earlier
        mIcons = Icons;
        name = Name;
        id = Email;
        pass = Pass;
        this.context = passedContext;
        im = imm; //here we assign those passed values to the values we declared here
        //in adapter


    }

    public String GetParse() {
        return parse;
    }

    @Override
    public MyAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row, parent, false); //Inflating the layout

            ViewHolder vhItem = new ViewHolder(v, viewType, context); //Creating ViewHolder and passing the object of type view

            return vhItem; // Returning the created object

            //inflate your layout and pass it to view holder

        } else if (viewType == TYPE_HEADER) {

            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.header, parent, false); //Inflating the layout

            ViewHolder vhHeader = new ViewHolder(v, viewType, context); //Creating ViewHolder and passing the object of type view

            return vhHeader; //returning the object created


        }
        return null;

    }


    //Below first we ovverride the method onCreateViewHolder which is called when the ViewHolder is
    //Created, In this method we inflate the item_row.xml layout if the viewType is Type_ITEM or else we inflate header.xml
    // if the viewType is TYPE_HEADER
    // and pass it to the view holder

    //Next we override a method which is called when the item in a row is needed to be displayed, here the int position
    // Tells us item at which position is being constructed to be displayed and the holder id of the holder object tell us
    // which view type is being created 1 for item row
    @Override
    public void onBindViewHolder(MyAdapter.ViewHolder holder, int position) {
        if (holder.Holderid == 1) {                              // as the list view is going to be called after the header view so we decrement the
            // position by 1 and pass it to the holder while setting the text and image
            holder.textView.setText(mNavTitles[position - 1]); // Setting the Text with the array of our Titles
            holder.imageView.setImageResource(mIcons[position - 1]);// Settimg the image with array of our icons
        } else {

            ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                    .memoryCacheExtraOptions(100, 100) // width, height
                    .discCacheExtraOptions(100, 100, Bitmap.CompressFormat.PNG, 100)
                    .build();

            ImageLoader imageLoader = ImageLoader.getInstance();
            imageLoader.init(config);
            imageLoader.displayImage(pass, holder.profile);
            //  holder.profile.setProfileId(id);
            holder.Name.setText(name);
            holder.id.setText(id);
        }
    }

    // This method returns the number of items present in the list
    @Override
    public int getItemCount() {
        return mNavTitles.length + 1; // the number of items in the list will be +1 the titles including the header view.
    }

    // Witht the following method we check what type of view is being passed
    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        int Holderid;

        TextView textView;
        ImageView imageView;
        ImageView profile;
        TextView Name;
        TextView id;
        String parsse;
        Context contxt;
        SharedPreferences sPref;
        String parserLink;


        public ViewHolder(View itemView, int ViewType, Context c) {                 // Creating ViewHolder Constructor with View and viewType As a parameter
            super(itemView);
            contxt = c;
            // Intent intent = ((Activity) contxt).getIntent();
            //  parserLink = intent.getExtras().getString("parseLink");
            itemView.setClickable(true);
            itemView.setOnClickListener(this);

            // Here we set the appropriate view in accordance with the the view type as passed when the holder object is created

            if (ViewType == TYPE_ITEM) {
                textView = (TextView) itemView.findViewById(R.id.rowText); // Creating TextView object with the id of textView from item_row.xml
                imageView = (ImageView) itemView.findViewById(R.id.rowIcon);// Creating ImageView object with the id of ImageView from item_row.xml
                Holderid = 1;
            } else {


                Name = (TextView) itemView.findViewById(R.id.name);         // Creating Text View object from header.xml for name
                id = (TextView) itemView.findViewById(R.id.email);       // Creating Text View object from header.xml for email
                profile = (ImageView) itemView.findViewById(R.id.imageView);// Creating Image view object from header.xml for profile pic
                Holderid = 0;                                                // Setting holder id = 0 as the object being populated are of type header view
            }
        }

        @Override
        public void onClick(View v) {
            Intent intent;
            switch (getPosition()) {
                case 1:
                    intent = new Intent(contxt, ProfileUser.class);
                    // intent.putExtra("passka", parserLink);
                    contxt.startActivity(intent);
                    break;
                case 2:
                    intent = new Intent(contxt, SettingsActivity.class);
                    // intent.putExtra("passka", parserLink);
                    contxt.startActivity(intent);
                    //Settings

                    break;
                case 3:
                    ((Activity) contxt).finish();
                    break;

            }

        }

    }


}