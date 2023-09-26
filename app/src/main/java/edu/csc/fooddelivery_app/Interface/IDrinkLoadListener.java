package edu.csc.fooddelivery_app.Interface;

import java.util.List;

import edu.csc.fooddelivery_app.Model.Drink;

public interface IDrinkLoadListener {
    void onDrinkLoadSuccess(List<Drink> drinkList);
    void onDrinkLoadFailed(String message);
}
