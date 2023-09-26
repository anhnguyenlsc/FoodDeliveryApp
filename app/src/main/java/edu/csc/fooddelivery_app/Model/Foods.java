package edu.csc.fooddelivery_app.Model;

import java.util.ArrayList;

public class Foods {
    public String name;
    public String surl;

    public Foods(String name, String surl) {
        this.name = name;
        this.surl = surl;
    }

    public Foods() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }
}
