package edu.csc.fooddelivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

import edu.csc.fooddelivery_app.Adapter.OrderAdapter;
import edu.csc.fooddelivery_app.Adapter.SelectFoodAdapter;
import edu.csc.fooddelivery_app.Interface.IOrderLoadListener;
import edu.csc.fooddelivery_app.Model.Order;
import edu.csc.fooddelivery_app.Model.SelectFood;

public class OrderPage extends AppCompatActivity implements IOrderLoadListener {
    RecyclerView recOrder, recView_addfoods;
    TextView tv_tongtien, tv_price_tamtinh, tv_phiship, tv_datmon;
    IOrderLoadListener iOrderLoadListener;

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private SelectFoodAdapter selectFoodAdapter;

    //BottomNavigation
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_page);

        recOrder = findViewById(R.id.recOrder);
        recView_addfoods = findViewById(R.id.recView_addfoods);
        tv_tongtien = findViewById(R.id.tv_tongtien);
        tv_price_tamtinh = findViewById(R.id.tv_price_tamtinh);
        tv_phiship = findViewById(R.id.tv_phiship);
        tv_datmon = findViewById(R.id.tv_datmon);

        //BottomNavigation
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_orders);

        init();
        loadCartProductFromDatabase();

        mRecyclerView = findViewById(R.id.recView_addfoods);
        mLayoutManager = new LinearLayoutManager(OrderPage.this,LinearLayoutManager.HORIZONTAL,false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<SelectFood> selectFoodFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<SelectFood>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Hamburgers"), SelectFood.class)
                .build();

        selectFoodAdapter = new SelectFoodAdapter(selectFoodFirebaseRecyclerOptions);
        mRecyclerView.setAdapter(selectFoodAdapter);

        tv_datmon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(OrderPage.this, Notify_Sucess.class);
                startActivity(intent);

                createOrder();
            }
        });

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId())
                {
                    case R.id.action_orders:
                        return true;

                    case R.id.action_home:
                        Intent iOrd = new Intent(getApplicationContext(), Homepage.class);
                        startActivity(iOrd);
                        overridePendingTransition(0,0);
                        return true;

                    case R.id.action_favorite:
                        Intent iFav = new Intent(getApplicationContext(), FavoritePage.class);
                        startActivity(iFav);
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

    private void createOrder() {
    }

    @Override
    protected void onStart() {
        super.onStart();
        selectFoodAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        selectFoodAdapter.stopListening();
    }

    private void init() {
        iOrderLoadListener = this;
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        recOrder.setLayoutManager(layoutManager);
        recOrder.addItemDecoration(new DividerItemDecoration(this, layoutManager.getOrientation()));
    }

    private void loadCartProductFromDatabase() {
        List<Order> orders = new ArrayList<>();
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists())
                        {
                            for (DataSnapshot orderSnapshot : snapshot.getChildren())
                            {
                                Order order = orderSnapshot.getValue(Order.class);
                                order.setKey(order.getKey());
                                orders.add(order);
                            }
                            iOrderLoadListener.onOrderLoadSuccess(orders);
                        }
                        else
                        {
                            iOrderLoadListener.onOrderLoadFailed("No Order in your Cart");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iOrderLoadListener.onOrderLoadFailed(error.getMessage());

                    }
                });
    }



    @Override
    public void onOrderLoadSuccess(List<Order> orderList) {
        double ship_fee = 10000;
        for (Order order : orderList)
        {
            ship_fee += order.getTotalPrice();
        }

        tv_price_tamtinh.setText(String.valueOf(ship_fee) + "đ");
        tv_tongtien.setText(String.valueOf(ship_fee) + "đ");

        OrderAdapter adapter = new OrderAdapter(this, orderList);
        recOrder.setAdapter(adapter);
    }

    @Override
    public void onOrderLoadFailed(String message) {

    }
}