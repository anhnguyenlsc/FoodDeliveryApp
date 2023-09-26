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
import edu.csc.fooddelivery_app.Interface.ICartLoadListener;
import edu.csc.fooddelivery_app.Model.Favorite;
import edu.csc.fooddelivery_app.MyUpdateCartEvent;
import edu.csc.fooddelivery_app.R;
import edu.csc.fooddelivery_app.Model.Rice;

public class RiceAdapter extends RecyclerView.Adapter<RiceAdapter.MyViewHolder> {
    Context context;
    List<Rice> riceList;
    ICartLoadListener iCartLoadListener;
    IFavoriteLoadListener2 iFavoriteLoadListener2;


    public RiceAdapter(Context context, List<Rice> riceList, ICartLoadListener iCartLoadListener, IFavoriteLoadListener iFavoriteLoadListener) {
        this.context = context;
        this.riceList = riceList;
        this.iCartLoadListener = iCartLoadListener;
        this.iFavoriteLoadListener2 = iFavoriteLoadListener2;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_rice, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(riceList.get(position).getSurl())
                .into(holder.iv_rice);

        holder.tv_name_rice.setText(riceList.get(position).getName());
        holder.tv_price_rice.setText(riceList.get(position).getPrice() + "đ");

        holder.btnAddToCart.setOnClickListener(view -> {
            RiceAdapter.this.addToCart(riceList.get(position));
        });

        holder.btnFavorite_Rice.setOnClickListener(view -> {
            RiceAdapter.this.addToFavorite(riceList.get(position));
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

    private void addToCart(Rice rice) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");

        userCart.child(rice.getKey())
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

                            userCart.child(rice.getKey())
                                    .updateChildren(updateData)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Added To Cart", Toast.LENGTH_LONG).show();
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
                            cart.setName(rice.getName());
                            cart.setSurl(rice.getSurl());
                            cart.setKey(rice.getKey());
                            cart.setPrice(rice.getPrice());
                            cart.setQuantity(1);
                            cart.setTotalPrice(Integer.parseInt(rice.getPrice()));

                            userCart.child(rice.getKey())
                                    .setValue(cart)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(context, "Added To Cart", Toast.LENGTH_LONG).show();
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
        return riceList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_name_rice, tv_price_rice;
        ImageView iv_rice, btnAddToCart, btnFavorite_Rice;

        public MyViewHolder(@NonNull View v) {
            super(v);

            tv_name_rice = v.findViewById(R.id.tv_name_rice);
            tv_price_rice = v.findViewById(R.id.tv_price_rice);
            iv_rice = v.findViewById(R.id.iv_rice);
            btnAddToCart = v.findViewById(R.id.btnAddToCart);
            btnFavorite_Rice = v.findViewById(R.id.btnFavorite_Rice);

        }
    }
}
