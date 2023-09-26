package edu.csc.fooddelivery_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.greenrobot.eventbus.EventBus;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import edu.csc.fooddelivery_app.Interface.IFavoriteLoadListener;
import edu.csc.fooddelivery_app.Interface.IFavoriteLoadListener2;
import edu.csc.fooddelivery_app.Model.Cart;
import edu.csc.fooddelivery_app.Model.Drink;
import edu.csc.fooddelivery_app.Interface.ICartLoadListener;
import edu.csc.fooddelivery_app.Interface.IRecyclerViewClickListener;
import edu.csc.fooddelivery_app.Model.Favorite;
import edu.csc.fooddelivery_app.Model.Rice;
import edu.csc.fooddelivery_app.MyUpdateCartEvent;
import edu.csc.fooddelivery_app.R;

public class DrinkAdapter extends RecyclerView.Adapter<DrinkAdapter.MyViewHolder> {
    Context context;
    List<Drink> drinkList;
    ICartLoadListener iCartLoadListener;
    IFavoriteLoadListener2 iFavoriteLoadListener2;


    public DrinkAdapter(Context context, List<Drink> drinkList, ICartLoadListener iCartLoadListener, IFavoriteLoadListener iFavoriteLoadListener) {
        this.context = context;
        this.drinkList = drinkList;
        this.iCartLoadListener = iCartLoadListener;
        this.iFavoriteLoadListener2 = iFavoriteLoadListener2;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder((LayoutInflater.from(context)
                .inflate(R.layout.layout_drink, parent, false)));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(drinkList.get(position).getSurl())
                .into(holder.iv_drink);

        holder.tv_name_drink.setText(drinkList.get(position).getName());
        holder.tv_price_drink.setText(drinkList.get(position).getPrice() + "đ");

        holder.btnAddToCart.setOnClickListener(view -> {
            DrinkAdapter.this.addToCart(drinkList.get(position));
        });

        holder.btnFavorite_Drink.setOnClickListener(view -> {
            DrinkAdapter.this.addToCart(drinkList.get(position));
        });
    }

    private void addToFavorite(Rice rice) {
        DatabaseReference userFavorite = FirebaseDatabase
                .getInstance()
                .getReference("Favorite")
                .child("FAVORITE_USER_ID");

        userFavorite.child(rice.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        Favorite favorite = new Favorite();
                        favorite.setName(rice.getName());
                        favorite.setSurl(rice.getSurl());
                        favorite.setKey(rice.getKey());
                        favorite.setPrice(rice.getPrice());

                        userFavorite.child(rice.getKey())
                                .setValue(favorite)
                                .addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(context, "Added To Favorite",  Toast.LENGTH_LONG).show();                                        }
                                })
                                .addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                    }
                                });
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iFavoriteLoadListener2.onFavoriteLoadFailed(error.getMessage());
                    }
                });
    }

    private void addToCart(Drink drink) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");

        userCart.child(drink.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) //If user already have item in cart
                        {
                            //Update quantity and total price
                            Cart cart = snapshot.getValue(Cart.class);
                            cart.setQuantity(cart.getQuantity() + 1);
                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("quantity", cart.getQuantity());
                            updateData.put("totalPrice", cart.getQuantity() * Integer.parseInt(cart.getPrice()));

                            userCart.child(drink.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Added To Cart", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            iCartLoadListener.onCartLoadFailed(e.getMessage());
                                        }
                                    });
                        } else //If item not in cart, add new
                        {
                            Cart cart = new Cart();
                            cart.setName(drink.getName());
                            cart.setSurl(drink.getSurl());
                            cart.setKey(drink.getKey());
                            cart.setPrice(drink.getPrice());
                            cart.setQuantity(1);
                            cart.setTotalPrice(Integer.parseInt(drink.getPrice()));

                            userCart.child(drink.getKey())
                                    .setValue(cart)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Added To Cart", Toast.LENGTH_LONG).show();
                                        }
                                    })
                                    .addOnFailureListener(e -> iCartLoadListener.onCartLoadFailed(e.getMessage()));
                        }
                        EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        iCartLoadListener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    @Override
    public int getItemCount() {
        return drinkList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name_drink, tv_price_drink;
        ImageView iv_drink, btnAddToCart, btnFavorite_Drink;

        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }
        public MyViewHolder(@NonNull View v) {
            super(v);

            tv_name_drink = v.findViewById(R.id.tv_name_drink);
            tv_price_drink = v.findViewById(R.id.tv_price_drink);
            iv_drink = v.findViewById(R.id.iv_drink);
            btnAddToCart = v.findViewById(R.id.btnAddToCart);
            btnFavorite_Drink = v.findViewById(R.id.btnFavorite_Drink);

        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerClick(v, getAbsoluteAdapterPosition());
        }
    }
}
