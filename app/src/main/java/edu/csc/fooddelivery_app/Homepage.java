package edu.csc.fooddelivery_app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;
import com.google.firebase.database.FirebaseDatabase;

import edu.csc.fooddelivery_app.Adapter.FlashAdapter;
import edu.csc.fooddelivery_app.Adapter.FoodAdapter;
import edu.csc.fooddelivery_app.Model.Flash;
import edu.csc.fooddelivery_app.Model.Foods;

public class Homepage extends AppCompatActivity {
    AutoCompleteTextView findfood;
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    //CardView for Category
    CardView cv_AnVat, cv_FastFood, cv_Healthy, cv_Inter, cv_Milktea, cv_Pho, cv_Rice, cv_SeaFood;

    //BottomNavigation
    BottomNavigationView bottomNavigationView;

    private FlashAdapter flashAdapter;
    private FoodAdapter foodAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepage);

        Resources res = getResources();
        String[] foodArray = res.getStringArray(R.array.food_array);

        findfood = (AutoCompleteTextView) findViewById(R.id.autoSearch);
        ArrayAdapter adapterFoods = new ArrayAdapter(this, android.R.layout.simple_list_item_1, foodArray);
        findfood.setAdapter(adapterFoods);

        //Đặt số ký tự tối thiểu để hiển thị các đề xuất
        findfood.setThreshold(1);

        //On after textchange
        findfood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Homepage.this, StorePage.class);
                startActivity(i);
            }
        });

        //Event for Category
        cv_AnVat = findViewById(R.id.cv_AnVat);
        cv_FastFood = findViewById(R.id.cv_FastFood);
        cv_Healthy = findViewById(R.id.cv_Healthy);
        cv_Inter = findViewById(R.id.cv_Inter);
        cv_Milktea = findViewById(R.id.cv_Milktea);
        cv_Pho = findViewById(R.id.cv_Pho);
        cv_Rice = findViewById(R.id.cv_Rice);
        cv_SeaFood = findViewById(R.id.cv_SeaFood);

        //BottomNavigation
        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setSelectedItemId(R.id.action_home);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.action_home:
                        return true;

                    case R.id.action_orders:
                        Intent iOrd = new Intent(getApplicationContext(), OrderPage.class);
                        startActivity(iOrd);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.action_favorite:
                        Intent iFav = new Intent(getApplicationContext(), FavoritePage.class);
                        startActivity(iFav);
                        overridePendingTransition(0, 0);
                        return true;

                    case R.id.action_account:
                        Intent iAcc = new Intent(getApplicationContext(), AccountPage.class);
                        startActivity(iAcc);
                        overridePendingTransition(0, 0);
                        return true;
                }
                return false;
            }
        });

        //Set sự kiện cho các nút chọn loại món ăn
        cv_AnVat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, AnVatPage.class);
                startActivity(intent);
            }
        });

        cv_FastFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, FastFoodPage.class);
                startActivity(intent);
            }
        });

        cv_Healthy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, HealthyPage.class);
                startActivity(intent);
            }
        });

        cv_Inter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, InterPage.class);
                startActivity(intent);
            }
        });

        cv_Milktea.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, DrinkPage.class);
                startActivity(intent);

            }
        });

        cv_Pho.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, PhoPage.class);
                startActivity(intent);

            }
        });

        cv_Rice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, RicePage.class);
                startActivity(intent);
            }
        });

        cv_SeaFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Homepage.this, SeaFoodPage.class);
                startActivity(intent);
            }
        });

        //RecyclerViewFlash
        mRecyclerView = findViewById(R.id.recViewflash);
        mLayoutManager = new LinearLayoutManager(Homepage.this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<Flash> flashFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Flash>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Flash"), Flash.class)
                .build();

        flashAdapter = new FlashAdapter(flashFirebaseRecyclerOptions);
        mRecyclerView.setAdapter(flashAdapter);

        //RecyclerViewFoods
        mRecyclerView = findViewById(R.id.recViewfoods);
        mLayoutManager = new LinearLayoutManager(Homepage.this, LinearLayoutManager.HORIZONTAL, false);

        mRecyclerView.setLayoutManager(mLayoutManager);

        FirebaseRecyclerOptions<Foods> foodsFirebaseRecyclerOptions = new FirebaseRecyclerOptions.Builder<Foods>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Foods"), Foods.class)
                .build();

        foodAdapter = new FoodAdapter(foodsFirebaseRecyclerOptions);
        mRecyclerView.setAdapter(foodAdapter);

    }

    @Override
    protected void onStart() {
        super.onStart();
        flashAdapter.startListening();
        foodAdapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        flashAdapter.stopListening();
        foodAdapter.startListening();
    }
}
