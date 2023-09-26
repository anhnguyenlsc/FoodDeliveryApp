package edu.csc.fooddelivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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

import edu.csc.fooddelivery_app.Adapter.CartAdapter;
import edu.csc.fooddelivery_app.Interface.ICartLoadListener;
import edu.csc.fooddelivery_app.Model.Cart;

public class CartActivity extends AppCompatActivity implements ICartLoadListener {
    ConstraintLayout mainLayout;
    RecyclerView recycler_cart;
    TextView txtTotal;
    Button btnOrder;
    ICartLoadListener cartLoadListener;

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

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onUpdateCart(MyUpdateCartEvent event)
    {
        loadCartFromFirebase();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        mainLayout = findViewById(R.id.mainLayout);
        recycler_cart = findViewById(R.id.recycler_cart);
        txtTotal = findViewById(R.id.txtTotal);
        btnOrder = findViewById(R.id.btnOrder);

        //Click vào chuyển đến trang Đơn hàng
        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(CartActivity.this, OrderPage.class);
                startActivity(i);
            }
        });


        init();
        loadCartFromFirebase();

    }

    private void loadCartFromFirebase() {
        List<Cart> carts = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot cartSnapshot : snapshot.getChildren())
                            {
                                Cart cartModel = cartSnapshot.getValue(Cart.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                carts.add(cartModel);
                            }
                            cartLoadListener.onCartLoadSuccess(carts);
                        }
                        else
                        {
                            cartLoadListener.onCartLoadFailed("Cart Empty");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        cartLoadListener.onCartLoadFailed(error.getMessage());

                    }
                });
    }

    private void init() {
        cartLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recycler_cart.setLayoutManager(layoutManager);
        recycler_cart.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }


    @Override
    public void onCartLoadSuccess(List<Cart> cartList) {
        double sum = 0;
        for (Cart cart : cartList)
        {
            sum += cart.getTotalPrice();
        }
        txtTotal.setText(String.valueOf(sum) + "đ");
        CartAdapter adapter = new CartAdapter(this, cartList);
        recycler_cart.setAdapter(adapter);

    }

    @Override
    public void onCartLoadSuccessNoti(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void onCartLoadFailed(String message) {
        Snackbar.make(mainLayout, message, Snackbar.LENGTH_LONG).show();
    }
}
