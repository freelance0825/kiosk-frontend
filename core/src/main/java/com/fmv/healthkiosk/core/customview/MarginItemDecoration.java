package com.fmv.healthkiosk.core.customview;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MarginItemDecoration extends RecyclerView.ItemDecoration {

    public enum LastPaddingToBeExcluded {
        NONE, TOP, BOTTOM, LEFT, RIGHT
    }

    private final int left;
    private final int right;
    private final int top;
    private final int bottom;
    private final LastPaddingToBeExcluded exclude;

    public MarginItemDecoration(int left, int right, int top, int bottom, LastPaddingToBeExcluded exclude) {
        this.left = left;
        this.right = right;
        this.top = top;
        this.bottom = bottom;
        this.exclude = exclude;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view,
                               @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int itemCount = state.getItemCount();

        outRect.set(left, top, right, bottom);

        if (position == itemCount - 1) {
            switch (exclude) {
                case TOP:
                    outRect.top = 0;
                    break;
                case BOTTOM:
                    outRect.bottom = 0;
                    break;
                case LEFT:
                    outRect.left = 0;
                    break;
                case RIGHT:
                    outRect.right = 0;
                    break;
                case NONE:
                default:
                    break;
            }
        }
    }
}
