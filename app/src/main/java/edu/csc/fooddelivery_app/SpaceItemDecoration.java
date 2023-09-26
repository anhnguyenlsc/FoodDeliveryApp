package edu.csc.fooddelivery_app;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class SpaceItemDecoration extends RecyclerView.ItemDecoration {
    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if(parent.getChildLayoutPosition(view) % 2 != 0) //Layout chia 2
        {
            //marginTop +50
            outRect.top = 50;
            //marginBottom -50
            outRect.bottom = -50;
        }
    }
}
