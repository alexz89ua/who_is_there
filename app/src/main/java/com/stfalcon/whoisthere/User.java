package com.stfalcon.whoisthere;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 22.04.15.
 */
public class User {
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;
    @SerializedName("link")
    public String link;

    public User(String name, String id, String link)
    {
        this.id=id;
        this.link=link;
        this.name=name;
    }

    public String Get_Pass_To_Profile_Foto()
    {
        return "http://graph.facebook.com/"+this.id+"/picture?type=large";
    }


}
