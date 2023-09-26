package edu.csc.fooddelivery_app.Interface;

import java.util.List;

import edu.csc.fooddelivery_app.Model.Cart;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<Cart> cartList);
    void onCartLoadSuccessNoti(String message);
    void onCartLoadFailed(String message);
}
