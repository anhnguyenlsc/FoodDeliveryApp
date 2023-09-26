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

import edu.csc.fooddelivery_app.Model.Flash;
import edu.csc.fooddelivery_app.R;

public class FlashAdapter extends FirebaseRecyclerAdapter<Flash,FlashAdapter.MyViewHolder> {

    /**
     * Initialize a {@link RecyclerView.Adapter} that listens to a Firebase query. See
     * {@link FirebaseRecyclerOptions} for configuration options.
     *
     * @param option
     */
    public FlashAdapter(@NonNull FirebaseRecyclerOptions<Flash> option) {
        super(option);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyViewHolder holder, int position, @NonNull Flash model) {
        holder.tv_names.setText(String.valueOf(model.getName()));

        Glide.with(holder.iv_flash.getContext())
                .load(model.getSurl())
                .into(holder.iv_flash);
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_flash, parent, false);
        return new MyViewHolder(view);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_names;
        ImageView iv_flash;

        public MyViewHolder(@NonNull View v) {
            super(v);

            tv_names = v.findViewById(R.id.tv_names);
            iv_flash = v.findViewById(R.id.iv_flash);
        }
    }
}
