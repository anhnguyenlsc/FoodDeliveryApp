package edu.csc.fooddelivery_app.Interface;

import java.util.List;

import edu.csc.fooddelivery_app.Model.Rice;

public interface IRiceLoadListener {
    void onRiceLoadSuccess(List<Rice> riceList);
    void onRiceLoadFailed(String message);
}
