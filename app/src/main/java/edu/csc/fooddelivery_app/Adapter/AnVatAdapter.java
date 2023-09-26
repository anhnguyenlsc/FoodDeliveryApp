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

import edu.csc.fooddelivery_app.Model.AnVat;
import edu.csc.fooddelivery_app.Model.Cart;
import edu.csc.fooddelivery_app.Model.Favorite;
import edu.csc.fooddelivery_app.Interface.ICartLoadListener;
import edu.csc.fooddelivery_app.Interface.IFavoriteLoadListener;
import edu.csc.fooddelivery_app.Interface.IFavoriteLoadListener2;
import edu.csc.fooddelivery_app.Interface.IRecyclerViewClickListener;
import edu.csc.fooddelivery_app.MyUpdateCartEvent;
import edu.csc.fooddelivery_app.R;

public class AnVatAdapter extends RecyclerView.Adapter<AnVatAdapter.MyDrinkHolder> {
    Context context;
    List<AnVat> anVatList;
    ICartLoadListener iCartLoadListener;
    IFavoriteLoadListener2 iFavoriteLoadListener2;

    public AnVatAdapter(Context context, List<AnVat> anVatList, ICartLoadListener iCartLoadListener, IFavoriteLoadListener iFavoriteLoadListener) {
        this.context = context;
        this.anVatList = anVatList;
        this.iCartLoadListener = iCartLoadListener;
        this.iFavoriteLoadListener2 = iFavoriteLoadListener2;
    }

    @NonNull
    @Override
    public MyDrinkHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyDrinkHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_anvat, parent, false ));
    }

    @Override
    public void onBindViewHolder(@NonNull MyDrinkHolder holder, int position) {
        Glide.with(context)
                .load(anVatList.get(position).getSurl())
                .into(holder.iv_av);

        holder.tv_name_av.setText(anVatList.get(position).getName());
        holder.tv_price_ava.setText(anVatList.get(position).getPrice() + "đ");

        holder.btnAddToCart.setOnClickListener(view -> {
            AnVatAdapter.this.addToCart(anVatList.get(position));
        });

        holder.btnFavorite.setOnClickListener(view -> {
            AnVatAdapter.this.addToFavorite(anVatList.get(position));
        });
    }
    private void addToFavorite(AnVat anVat) {
        DatabaseReference userFavorite = FirebaseDatabase
                .getInstance()
                .getReference("Favorite")
                .child("FAVORITE_USER_ID");

        userFavorite.child(anVat.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                            Favorite favorite = new Favorite();
                            favorite.setName(anVat.getName());
                            favorite.setSurl(anVat.getSurl());
                            favorite.setKey(anVat.getKey());
                            favorite.setPrice(anVat.getPrice());

                        userFavorite.child(anVat.getKey())
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

    private void addToCart(AnVat anVat) {
        DatabaseReference userCart = FirebaseDatabase
                .getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID");

        userCart.child(anVat.getKey())
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

                            userCart.child(anVat.getKey())
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
                        }
                        else //If item not in cart, add new
                        {
                            Cart cart = new Cart();
                            cart.setName(anVat.getName());
                            cart.setSurl(anVat.getSurl());
                            cart.setKey(anVat.getKey());
                            cart.setPrice(anVat.getPrice());
                            cart.setQuantity(1);
                            cart.setTotalPrice(Integer.parseInt(anVat.getPrice()));

                            userCart.child(anVat.getKey())
                                    .setValue(cart)
                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Toast.makeText(context, "Added To Cart", Toast.LENGTH_LONG).show();                                        }
                                    })
                                    .addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            iCartLoadListener.onCartLoadFailed(e.getMessage());
                                        }
                                    });
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
        return anVatList.size();
    }

    public class MyDrinkHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tv_name_av, tv_price_ava;
        ImageView iv_av, btnAddToCart, btnFavorite;

        IRecyclerViewClickListener listener;

        public void setListener(IRecyclerViewClickListener listener) {
            this.listener = listener;
        }

        public MyDrinkHolder(@NonNull View v) {
            super(v);
            tv_name_av = v.findViewById(R.id.tv_name_av);
            tv_price_ava = v.findViewById(R.id.tv_price_av);
            iv_av = v.findViewById(R.id.iv_av);
            btnAddToCart = v.findViewById(R.id.btnAddToCart);
            btnFavorite = v.findViewById(R.id.btnFavorite);
        }

        @Override
        public void onClick(View v) {
            listener.onRecyclerClick(v, getAbsoluteAdapterPosition());
        }
    }
}
