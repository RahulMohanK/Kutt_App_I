package com.example.root.kutt_app_i;

public class ListenItem {
    private String Link,Title;
    private int Star;
    private byte[] Icon;


    public ListenItem(String link ,int star,String title,byte[] icon) {
        Link = link;
        Title = title;
        Star = star;
        Icon = icon;
    }

    public String getLink(){
        return Link;
    }
    public String getTitle(){
        return Title;
    }
    public int getStar(){
        return Star;
    }
    public byte[] getIcon(){
        return Icon;
    }


}