package edu.csc.fooddelivery_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import edu.csc.fooddelivery_app.Adapter.HamburgerAdapter;
import edu.csc.fooddelivery_app.Model.Hamburgers;

public class StorePage extends AppCompatActivity {

    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;

    private ArrayList<Hamburgers> hamburgerList;
    private HamburgerAdapter hamburgerAdapter;

    //BottomNavigation
    BottomNavigationView bottomNavigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_page);
        mRecyclerView = findViewById(R.id.recyclerView);

        //RecyclerView : GridView
        mLayoutManager = new GridLayoutManager(this,2);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setFocusable(false);
        mRecyclerView.setNestedScrollingEnabled(false);

        hamburgerList = Hamburgers.init();
        hamburgerAdapter = new HamburgerAdapter(StorePage.this, R.layout.item_hamburger, hamburgerList);
        mRecyclerView.setAdapter(hamburgerAdapter);

//        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId())
//                {
//                    case R.id.action_home:
//                        return true;
//
//                    case R.id.action_orders:
//                        Intent iOrd = new Intent(getApplicationContext(), OrderPage.class);
//                        startActivity(iOrd);
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.action_favorite:
//                        Intent iFav = new Intent(getApplicationContext(), FavoritePage.class);
//                        startActivity(iFav);
//                        overridePendingTransition(0,0);
//                        return true;
//
//                    case R.id.action_account:
//                        Intent iAcc = new Intent(getApplicationContext(), AccountPage.class);
//                        startActivity(iAcc);
//                        overridePendingTransition(0,0);
//                        return true;
//                }
//                return false;
//            }
//        });
    }
}