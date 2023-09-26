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

import edu.csc.fooddelivery_app.Model.Foods;
import edu.csc.fooddelivery_app.R;

public class FoodAdapter extends FirebaseRecyclerAdapter<Foods,FoodAdapter.MyViewHolder> {
    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param options
     */
    public FoodAdapter(@NonNull FirebaseRecyclerOptions<Foods> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Foods model) {
        holder.tv_namefood.setText(String.valueOf(model.getName()));

        Glide.with(holder.iv_food.getContext())
                .load(model.getSurl())
                .into(holder.iv_food);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_foods, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_namefood;
        ImageView iv_food;

        public MyViewHolder(@NonNull View v) {
            super(v);
            tv_namefood = v.findViewById(R.id.tv_namefood);
            iv_food = v.findViewById(R.id.iv_food);

        }
    }
}
