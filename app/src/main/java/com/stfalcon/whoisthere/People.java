package com.stfalcon.whoisthere;

import com.google.gson.annotations.SerializedName;

/**
 * Created by root on 06.05.15.
 */
public class People {
    @SerializedName("name")
    public String name;
    @SerializedName("id")
    public String id;
    @SerializedName("x")
    public String x;
    @SerializedName("y")
    public String y;

    public People(String id, String name, String x, String y)
    {
        this.id=id;
        this.name=name;
        this.x=x;
        this.y = y;
    }


}
