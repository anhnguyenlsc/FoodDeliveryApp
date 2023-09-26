package edu.csc.fooddelivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.ImageView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.nex3z.notificationbadge.NotificationBadge;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

import edu.csc.fooddelivery_app.Adapter.RiceAdapter;
import edu.csc.fooddelivery_app.Interface.ICartLoadListener;
import edu.csc.fooddelivery_app.Interface.IFavoriteLoadListener;
import edu.csc.fooddelivery_app.Interface.IRiceLoadListener;
import edu.csc.fooddelivery_app.Model.Cart;
import edu.csc.fooddelivery_app.Model.Rice;

public class RicePage extends AppCompatActivity implements IRiceLoadListener, ICartLoadListener {
    //BottomNavigation
    BottomNavigationView bottomNavigationView;

    private RecyclerView mRecyclerView;

    IRiceLoadListener iRiceLoadListener;
    ICartLoadListener iCartLoadListener;
    IFavoriteLoadListener iFavoriteLoadListener;

    ConstraintLayout mainLayout_Rice;

    NotificationBadge badge;
    ImageView btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rice_page);
        mRecyclerView = findViewById(R.id.recRice);

        btnCart = findViewById(R.id.btnCart);
        badge = findViewById(R.id.badge);
        mainLayout_Rice = findViewById(R.id.mainLayout_Rice);

        //BottomNavigation
        bottomNavigationView = findViewById(R.id.bottom_nav);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_account:
                        Intent iAcc = new Intent(getApplicationContext(), AccountPage.class);
                        startActivity(iAcc);
                        overridePendingTransition(0,0);
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

                    case R.id.action_favorite:
                        Intent iFav = new Intent(getApplicationContext(), FavoritePage.class);
                        startActivity(iFav);
                        overridePendingTransition(0,0);
                        return true;
                }
                return false;
            }
        });

        init();
        loadRicefromDatabase();
        countCartItem();
    }

    public void init() {
        iRiceLoadListener = this;
        iCartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());

        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }

    private void loadRicefromDatabase() {
        List<Rice> rices = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Rice")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot riceSnapshot:snapshot.getChildren())
                            {
                                Rice rice = riceSnapshot.getValue(Rice.class);
                                rice.setKey(riceSnapshot.getKey());
                                rices.add(rice);
                            }
                            iRiceLoadListener.onRiceLoadSuccess(rices);
                        }
                        else
                            iRiceLoadListener.onRiceLoadFailed("Can't find food");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iRiceLoadListener.onRiceLoadFailed(error.getMessage());
                    }
                });
    }

    private void countCartItem() {
        List<Cart> carts = new ArrayList<>();
        FirebaseDatabase
                .getInstance().getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot cartSnapshot : snapshot.getChildren())
                        {
                            Cart cart = cartSnapshot.getValue(Cart.class);
                            cart.setKey(cartSnapshot.getKey());
                            carts.add(cart);
                        }
                        iCartLoadListener.onCartLoadSuccess(carts);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (EventBus.getDefault().hasSubscriberForEvent(MyUpdateCartEvent.class))
            EventBus.getDefault().removeStickyEvent(MyUpdateCartEvent.class);
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        countCartItem();
    }


    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        countCartItem();
    }

    @Override
    public void onRiceLoadSuccess(List<Rice> riceList) {
        RiceAdapter adapter = new RiceAdapter(this, riceList, iCartLoadListener, iFavoriteLoadListener);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onRiceLoadFailed(String message) {
        Snackbar.make(mainLayout_Rice, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadSuccess(List<Cart> cartList) {
        int cartSum = 0;
        for (Cart cart : cartList)
            cartSum += cart.getQuantity();
        badge.setNumber(cartSum);

    }

    @Override
    public void onCartLoadSuccessNoti(String message) {
        Snackbar.make(mainLayout_Rice, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadFailed(String message) {
        //Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }
}