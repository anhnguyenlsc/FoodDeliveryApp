package edu.csc.fooddelivery_app.Model;

public class Rice {
    public String key, name, price, surl;

    public Rice(String key, String name, String price, String surl) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.surl = surl;
    }

    public Rice() {
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getSurl() {
        return surl;
    }

    public void setSurl(String surl) {
        this.surl = surl;
    }
}
