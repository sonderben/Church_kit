package com.churchkit.churchkit.ui.util;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class GridSpacingIDeco extends RecyclerView.ItemDecoration {

    private int spanCount;
    private int spacing;
    private boolean includeEdge;

    public GridSpacingIDeco(/*int spanCount, */int spacing/*, boolean includeEdge*/) {
        this.spanCount = spanCount;
        this.spacing = spacing;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {

       //GridLayoutManager gridLayoutManager= (GridLayoutManager) parent.getLayoutManager();


        /*int position = parent.getChildAdapterPosition(view); // item position
        int column = position % spanCount; // item column*/

        //outRect.bottom = spacing;
        outRect.top = spacing;
        /*if (gridLayoutManager.getSpanCount()>1){
            if (includeEdge) {
                outRect.left = spacing - column * spacing / spanCount;
                outRect.right = (column + 1) * spacing / spanCount;

                if (position < spanCount) {
                    outRect.top = spacing;
                }
                outRect.bottom = spacing;
            } else {
                outRect.left = column * spacing / spanCount;
                outRect.right = spacing - (column + 1) * spacing / spanCount;
                if (position >= spanCount) {
                    outRect.top = spacing;
                }
            }
        }else {

            if (position >= 0)
                outRect.top = spacing; // item top

        }*/


    }
}
