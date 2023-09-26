package edu.csc.fooddelivery_app.Interface;

import java.util.List;

import edu.csc.fooddelivery_app.Model.Favorite;

public interface IFavoriteLoadListener2 {
    void onFavoriteLoadSuccess(List<Favorite> favoriteList);
    void onFavoriteLoadSuccessNoti(String message);
    void onFavoriteLoadFailed(String message);
}
