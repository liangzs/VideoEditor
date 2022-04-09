package com.qiusuo.videoeditor.widgegt;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemDecoration extends RecyclerView.ItemDecoration {

    //是否有水平间隙
    private boolean decorationH;
    //是否有垂直间隙
    private boolean decorationV;
    //间隙值
    private int space;
    //头部额外间隙值
    private int topSpace;
    //尾部额外间隙值
    private int bottomSpace;
    //是否竖直列表
    private boolean isVertical;

    public RecyclerItemDecoration(int space, boolean decorationH, boolean decorationV) {
        this(space, decorationH, decorationV, 0, 0, true);
    }

    public RecyclerItemDecoration(int space, boolean decorationH, boolean decorationV, int topSpace, int bottomSpace, boolean isVertical) {
        this.space = space;
        this.decorationH = decorationH;
        this.decorationV = decorationV;
        this.topSpace = topSpace;
        this.bottomSpace = bottomSpace;
        this.isVertical = isVertical;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        //获取layoutParams参数
        RecyclerView.LayoutParams layoutParams = (RecyclerView.LayoutParams) view.getLayoutParams();
        //当前位置
        int itemPosition = layoutParams.getViewLayoutPosition();
        //Item数量
        int childCount = parent.getAdapter().getItemCount();
        if (decorationH) {
            outRect.left = space;
            outRect.right = space;
            if (itemPosition == 0 && !isVertical) {
                outRect.left += topSpace;
            }
            if (itemPosition == childCount - 1 && !isVertical) {
                outRect.right += bottomSpace;
            }
        }
        if (decorationV) {
            outRect.top = space;
            outRect.bottom = space;
            if (itemPosition == 0 && isVertical) {
                outRect.top += topSpace;
            }
            if (itemPosition == childCount - 1 && isVertical) {
                outRect.bottom += bottomSpace;
            }
        }
    }
}
