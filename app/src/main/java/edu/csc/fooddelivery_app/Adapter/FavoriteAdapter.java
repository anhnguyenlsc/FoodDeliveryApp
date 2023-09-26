package edu.csc.fooddelivery_app.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

import edu.csc.fooddelivery_app.Model.Favorite;
import edu.csc.fooddelivery_app.R;

public class FavoriteAdapter extends RecyclerView.Adapter<FavoriteAdapter.MyViewHolder>{
    Context context;
    public List<Favorite> favoriteList;

    public FavoriteAdapter(Context context, List<Favorite> favoriteList) {
        this.context = context;
        this.favoriteList = favoriteList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(context)
                .inflate(R.layout.layout_favorite, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context)
                .load(favoriteList.get(position).getSurl())
                .into(holder.iv_FavProduct);

        holder.tv_NameFavProduct.setText(String.valueOf(favoriteList.get(position).getName()));
        holder.tv_PriceFavProduct.setText(String.valueOf(favoriteList.get(position).getPrice() + " đ"));
    }



    @Override
    public int getItemCount() {
        return favoriteList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tv_NameFavProduct, tv_PriceFavProduct;
        ImageView iv_FavProduct;
        public MyViewHolder(@NonNull View v) {
            super(v);

            tv_NameFavProduct = v.findViewById(R.id.tv_NameFavProduct);
            tv_PriceFavProduct = v.findViewById(R.id.tv_PriceFavProduct);
            iv_FavProduct = v.findViewById(R.id.iv_FavProduct);
        }
    }
}
