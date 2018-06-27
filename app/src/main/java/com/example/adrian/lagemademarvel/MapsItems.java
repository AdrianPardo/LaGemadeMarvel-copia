package com.example.adrian.lagemademarvel;


public class MapsItems {

    private String email, name, telph, direction;

    public String getMapsEmail(){return email;}

    public String getMapsName(){return name;}

    public String getMapsdirec(){return direction;}

    public String getMapsTelefono(){return telph;}

    MapsItems(String name, String direc, String mail, String tel){
        this.email=mail;
        this.name=name;
        this.direction=direc;
        this.telph=tel;
    }
}