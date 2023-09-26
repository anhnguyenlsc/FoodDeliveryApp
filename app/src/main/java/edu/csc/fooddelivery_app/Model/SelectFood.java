package edu.csc.fooddelivery_app.Model;

public class SelectFood {
    public String name, price;
    public String surl;

    public SelectFood(String name, String price, String surl) {
        this.name = name;
        this.price = price;
        this.surl = surl;
    }

    public SelectFood() {
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
