package edu.csc.fooddelivery_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.Random;

import edu.csc.fooddelivery_app.Model.Order;
import edu.csc.fooddelivery_app.R;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder>{
    Context context;
    public List<Order> orderList;

    public OrderAdapter(Context context, List<Order> orderList) {
        this.context = context;
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_order, parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.tv_tenmon.setText(String.valueOf(orderList.get(position).getName()));
        holder.tv_soluong.setText(String.valueOf(orderList.get(position).getQuantity() + " x"));
        holder.tv_gia.setText(String.valueOf(orderList.get(position).getTotalPrice()));

//        holder.tv_datmon.setOnClickListener(view -> {
//            OrderAdapter.this.createOrder(orderList.get(position));
//        });
    }

    private void createOrder(Order order) {
        Random rd = new Random();
        int ranID = rd.nextInt();

        DatabaseReference userOrder = FirebaseDatabase
                .getInstance()
                .getReference("Orders")
                .child("OrderID" + ranID);
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_tenmon, tv_soluong, tv_gia, tv_datmon;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_tenmon = v.findViewById(R.id.tv_tenmon);
            tv_soluong = v.findViewById(R.id.tv_soluong);
            tv_gia = v.findViewById(R.id.tv_gia);
            tv_datmon = v.findViewById(R.id.tv_datmon);
        }
    }
}
