package edu.csc.fooddelivery_app.Model;

public class Cart {
    public String  key, name, price, surl;
    public int quantity;
    public int totalPrice;

    public Cart(String key, String name, String price, String surl, int quantity, int totalPrice) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.surl = surl;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public Cart() {
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

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public float getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }
}
