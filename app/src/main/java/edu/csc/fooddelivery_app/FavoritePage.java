package edu.csc.fooddelivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import edu.csc.fooddelivery_app.Adapter.FavoriteAdapter;
import edu.csc.fooddelivery_app.Interface.IFavoriteLoadListener2;
import edu.csc.fooddelivery_app.Model.Favorite;

public class FavoritePage extends AppCompatActivity implements IFavoriteLoadListener2 {
    RecyclerView recFav;
    FavoritePage iFavoriteLoadListener;
    ConstraintLayout mainLayout;

    //BottomNavigation
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateFavoriteEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateFavoriteEvent.class);
        EventBus.getDefault().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateFavorite(MyUpdateFavoriteEvent event)
    {
        loadFavoriteFromDatabase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorite_page);
        mainLayout = findViewById(R.id.mainLayout);

        recFav = findViewById(R.id.recFav);

        init();
        loadFavoriteFromDatabase();

        //BottomNavigation
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_favorite);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_favorite:
                        return true;

                    case R.id.action_orders:
                        Intent iOrd = new Intent(getApplicationContext(), OrderPage.class);
                        startActivity(iOrd);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.action_home:
                        Intent iHome = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(iHome);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.action_account:
                        Intent iAcc = new Intent(getApplicationContext(), AccountPage.class);
                        startActivity(iAcc);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });
    }

    private void loadFavoriteFromDatabase() {
        List<Favorite> favorites = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Favorite")
                //Tạo một FAVORITE_USER_ID chứa các món đã bấm yêu thích
                .child("FAVORITE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot favSnapshot : snapshot.getChildren())
                            {
                                Favorite favorite = favSnapshot.getValue(Favorite.class);
                                favorite.setKey(favorite.getKey());
                                favorites.add(favorite);
                            }
                            iFavoriteLoadListener.onFavoriteLoadSuccess(favorites);
                        }
                        else
                        {
                            iFavoriteLoadListener.onFavoriteLoadFailed("No Favorite Food");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iFavoriteLoadListener.onFavoriteLoadFailed(error.getMessage());

                    }
                });
    }

    private void init() {
        iFavoriteLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recFav.setLayoutManager(layoutManager);
        recFav.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    @Override
    public void onFavoriteLoadSuccess(List<Favorite> favoriteList) {
        FavoriteAdapter adapter = new FavoriteAdapter(this, favoriteList);
        recFav.setAdapter(adapter);
    }

    @Override
    public void onFavoriteLoadSuccessNoti(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onFavoriteLoadFailed(String message) {

    }
}