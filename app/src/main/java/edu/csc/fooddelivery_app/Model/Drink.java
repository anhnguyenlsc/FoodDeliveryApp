package edu.csc.fooddelivery_app.Model;

public class Drink {
    String key, name, price, surl;

    public Drink(String key, String name, String price, String surl) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.surl = surl;
    }

    public Drink() {
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
