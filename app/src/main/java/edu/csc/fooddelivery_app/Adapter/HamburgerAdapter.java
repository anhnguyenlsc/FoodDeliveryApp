package edu.csc.fooddelivery_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import edu.csc.fooddelivery_app.Model.Hamburgers;
import edu.csc.fooddelivery_app.R;

public class HamburgerAdapter extends RecyclerView.Adapter<HamburgerAdapter.ViewHolder> {
    Context context;
    int layout;
    ArrayList<Hamburgers> arrayList;

    public HamburgerAdapter(Context context, int layout, ArrayList<Hamburgers> arrayList) {
        this.context = context;
        this.layout = layout;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public HamburgerAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_hamburger, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull HamburgerAdapter.ViewHolder holder, int position) {
        Hamburgers hamburger = arrayList.get(position);
        holder.tvName.setText(String.valueOf(hamburger.getName()));
        holder.tvPrice.setText(String.valueOf(hamburger.getPrice()));
        holder.imageHam.setImageResource(hamburger.getPic());
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView tvName, tvPrice;
        public ImageView imageHam;

        public ViewHolder(@NonNull View v) {
            super(v);
            tvName = v.findViewById(R.id.tvName);
            tvPrice = v.findViewById(R.id.tvPrice);
            imageHam = v.findViewById(R.id.imageHam);

        }
    }
}
