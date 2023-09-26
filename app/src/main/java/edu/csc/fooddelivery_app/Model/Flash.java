package edu.csc.fooddelivery_app.Model;

import java.util.ArrayList;

public class Flash {
    public String name;
    public String surl;

    public Flash(String name, String surl) {
        this.name = name;
        this.surl = surl;
    }

    public Flash(){}

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
