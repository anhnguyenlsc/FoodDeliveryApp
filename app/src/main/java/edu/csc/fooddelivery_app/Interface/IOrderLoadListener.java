package edu.csc.fooddelivery_app.Interface;

import java.util.List;

import edu.csc.fooddelivery_app.Model.Order;

public interface IOrderLoadListener {
    void onOrderLoadSuccess(List<Order> orderList);
    void onOrderLoadFailed(String message);
}
