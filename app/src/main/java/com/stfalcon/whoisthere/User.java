package com.stfalcon.whoisthere;

/**
 * Created by root on 22.04.15.
 */
public class User {
    private String name;
    private String id;
    private String link;

    public User(String name, String id, String link)
    {
        this.id=id;
        this.link=link;
        this.name=name;
    }

    public String getName()
    {
        return name;
    }

    public String getId()
    {
        return id;
    }

    public String getLink()
    {
        return link;
    }



}
