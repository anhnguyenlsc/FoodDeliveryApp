package edu.csc.fooddelivery_app.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.firebase.database.FirebaseDatabase;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import edu.csc.fooddelivery_app.Model.Cart;
import edu.csc.fooddelivery_app.MyUpdateCartEvent;
import edu.csc.fooddelivery_app.R;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.MyViewHolder> {
    Context context;
    public List<Cart> cartList;

    public CartAdapter(Context context, List<Cart> cartList) {
        this.context = context;
        this.cartList = cartList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_cart, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(cartList.get(position).getSurl())
                .into(holder.imageProduct);

        holder.txtNameProduct.setText(String.valueOf(cartList.get(position).getName()));
        holder.txtPriceProduct.setText(String.valueOf(cartList.get(position).getPrice()) + "đ");
        holder.txtQuantity.setText(String.valueOf(cartList.get(position).getQuantity()));

        //Event
        holder.btnMinus.setOnClickListener(v -> {
            minusCartItem(holder, cartList.get(position));
        });

        holder.btnPlus.setOnClickListener(v -> {
            plusCartItem(holder, cartList.get(position));
        });

        holder.btnDelete.setOnClickListener(v -> {
            AlertDialog dialog = new AlertDialog.Builder(context)
                    .setTitle("Delete item")
                    .setMessage("Do you want to delete this item ?")
                    .setNegativeButton("CANCEL", (dialogInterface, which) -> dialogInterface.dismiss())
                    .setPositiveButton("Ok", (dialogInterface, i) -> {

                        //Temp remove
                        notifyItemRemoved(position);

                        deleteFromFirebase(cartList.get(position));
                        dialogInterface.dismiss();
                    }).create();
            dialog.show();
        });

    }

    private void deleteFromFirebase(Cart cart) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .child(cart.getKey())
                .removeValue()
                .addOnSuccessListener((Void aVoid) -> {
                    EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                });
    }

    private void plusCartItem(MyViewHolder holder, Cart cart) {
        cart.setQuantity(cart.getQuantity() + 1);
        cart.setTotalPrice(cart.getQuantity() * Integer.parseInt(cart.getPrice()));

        //Update quantity
        holder.txtQuantity.setText(new StringBuilder().append(cart.getQuantity()));
        updateFirebase(cart);
    }

    private void minusCartItem(MyViewHolder holder, Cart cart) {
        if (cart.getQuantity() > 1)
        {
            cart.setQuantity(cart.getQuantity() - 1);
            cart.setTotalPrice(cart.getQuantity() * Integer.parseInt(cart.getPrice()));

            //Update quantity
            holder.txtQuantity.setText(new StringBuilder().append(cart.getQuantity()));
            updateFirebase(cart);
        }
    }

    private void updateFirebase(Cart cart) {
        FirebaseDatabase.getInstance()
                .getReference("Cart")
                .child("UNIQUE_USER_ID")
                .child(cart.getKey())
                .setValue(cart)
                .addOnSuccessListener(aVoid -> {
                    EventBus.getDefault().postSticky(new MyUpdateCartEvent());
                });
    }

    @Override
    public int getItemCount() {
        return cartList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView txtNameProduct, txtPriceProduct, txtQuantity;
        ImageView imageProduct, btnMinus, btnPlus, btnDelete;

        public MyViewHolder(@NonNull View v) {
            super(v);

            txtNameProduct = v.findViewById(R.id.txtNameProduct);
            txtPriceProduct = v.findViewById(R.id.txtPriceProduct);
            txtQuantity = v.findViewById(R.id.txtQuantity);

            imageProduct = v.findViewById(R.id.imageProduct);
            btnMinus = v.findViewById(R.id.btnMinus);
            btnPlus = v.findViewById(R.id.btnPlus);
            btnDelete = v.findViewById(R.id.btnDelete);
        }
    }
}
