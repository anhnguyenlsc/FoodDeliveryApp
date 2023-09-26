package edu.csc.fooddelivery_app.Model;

public class Order {
    String key, name, price, price_temp;
    int quantity, totalPrice, ship_fee;

    public Order(String key, String name, String price, String price_temp, int quantity, int totalPrice, int ship_fee) {
        this.key = key;
        this.name = name;
        this.price = price;
        this.price_temp = price_temp;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.ship_fee = ship_fee;
    }

    public Order() {
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

    public String getPrice_temp() {
        return price_temp;
    }

    public void setPrice_temp(String price_temp) {
        this.price_temp = price_temp;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity_order(int quantity) {
        this.quantity = quantity;
    }

    public int getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(int totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getShip_fee() {
        return ship_fee;
    }

    public void setShip_fee(int ship_fee) {
        this.ship_fee = ship_fee;
    }
}
