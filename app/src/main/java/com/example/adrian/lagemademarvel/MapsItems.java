package com.example.adrian.lagemademarvel;


public class MapsItems {

    private String email, name, horario, telph, direction;

    public String getMapsEmail(){return email;}

    public String getMapsName(){return name;}

    public String getMapsdirec(){return direction;}

    public String getMapsHora(){return horario;}

    public String getMapsTelefono(){return telph;}

    MapsItems(String name, String direc, String mail, String hora, String tel){
        this.email=mail;
        this.name=name;
        this.direction=direc;
        this.horario=hora;
        this.telph=tel;
    }
}