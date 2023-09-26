package edu.csc.fooddelivery_app.Interface;

import java.util.List;

import edu.csc.fooddelivery_app.Model.AnVat;

public interface IAnVatLoadListener {
    void onAnVatLoadSuccess(List<AnVat> anVatList);
    void onAnVatLoadFailed(String message);
}
