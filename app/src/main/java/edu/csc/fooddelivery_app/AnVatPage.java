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

import edu.csc.fooddelivery_app.Adapter.AnVatAdapter;
import edu.csc.fooddelivery_app.Interface.IAnVatLoadListener;
import edu.csc.fooddelivery_app.Interface.ICartLoadListener;
import edu.csc.fooddelivery_app.Interface.IFavoriteLoadListener;
import edu.csc.fooddelivery_app.Model.AnVat;
import edu.csc.fooddelivery_app.Model.Cart;

public class AnVatPage extends AppCompatActivity implements IAnVatLoadListener, ICartLoadListener {
    //BottomNavigation
    BottomNavigationView bottomNavigationView;
    private RecyclerView mRecyclerView;

    IAnVatLoadListener iAnVatLoadListener;
    ICartLoadListener iCartLoadListener;
    IFavoriteLoadListener iFavoriteLoadListener;

    //Trang Layout chính
    ConstraintLayout mainLayout;

    //Chấm đỏ thông báo số lượng sản phẩm hiện có trong giỏ hàng
    NotificationBadge badge;
    ImageView btnCart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_an_vat_page);
        mRecyclerView = findViewById(R.id.recAnVat);

        mainLayout = findViewById(R.id.mainLayout);
        badge = findViewById(R.id.badge);
        btnCart = findViewById(R.id.btnCart);
        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));

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
        //Load AnVat from Database
        loadAnVatfromDatabse();
        countCartItem();
    }

    public void init() {
        iAnVatLoadListener = this;
        iCartLoadListener = this;

        GridLayoutManager gridLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(gridLayoutManager);
        //Set vị trí cho các item đứng chéo xen kẽ nhau
        mRecyclerView.addItemDecoration(new SpaceItemDecoration());

        btnCart.setOnClickListener(v -> startActivity(new Intent(this, CartActivity.class)));
    }


    private void loadAnVatfromDatabse() {
        List<AnVat> anVats = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("AnVat")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists())
                        {
                            for (DataSnapshot anvatSnapshot:snapshot.getChildren())
                            {
                                AnVat anVat = anvatSnapshot.getValue(AnVat.class);
                                anVat.setKey(anvatSnapshot.getKey());
                                anVats.add(anVat);
                            }
                            iAnVatLoadListener.onAnVatLoadSuccess(anVats);
                        }
                        else
                            iAnVatLoadListener.onAnVatLoadFailed("Can't find food");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iAnVatLoadListener.onAnVatLoadFailed(error.getMessage());
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
    public void onCartLoadSuccess(List<Cart> cartList) {
        int cartSum = 0;
        for (Cart cart : cartList)
            cartSum += cart.getQuantity();
        badge.setNumber(cartSum);
    }

    @Override
    public void onCartLoadSuccessNoti(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onAnVatLoadSuccess(List<AnVat> anVatList) {
        AnVatAdapter adapter = new AnVatAdapter(this, anVatList, iCartLoadListener, iFavoriteLoadListener);
        mRecyclerView.setAdapter(adapter);
    }

    @Override
    public void onAnVatLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }
}