package edu.csc.fooddelivery_app.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import edu.csc.fooddelivery_app.R;
import edu.csc.fooddelivery_app.Model.SelectFood;

public class SelectFoodAdapter extends FirebaseRecyclerAdapter<SelectFood,SelectFoodAdapter.MyViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public SelectFoodAdapter(@NonNull FirebaseRecyclerOptions<SelectFood> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull SelectFood model) {
        holder.tv_namebur.setText(String.valueOf(model.getName()));
        holder.tv_price_bur.setText(String.valueOf(model.getPrice()) + "đ");

        Glide.with(holder.iv_burger.getContext())
                .load(model.getSurl())
                .into(holder.iv_burger);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_orderpage, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_namebur, tv_price_bur;
        ImageView iv_burger;
        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_namebur = v.findViewById(R.id.tv_namebur);
            tv_price_bur = v.findViewById(R.id.tv_price_bur);
            iv_burger = v.findViewById(R.id.iv_burger);

        }
    }
}
