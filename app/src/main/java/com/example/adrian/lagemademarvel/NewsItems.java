package com.example.adrian.lagemademarvel;


public class NewsItems {

    private String image, title, info, date;

    public String getImage(){return image;}

    public String getNewsTitle(){return title;}

    public String getNewsInfo(){return info;}

    public String getDate(){return date;}

    NewsItems(String ttl, String info, String img, String date){
        this.image=img;
        this.title=ttl;
        this.info=info;
        this.date=date;
    }
}