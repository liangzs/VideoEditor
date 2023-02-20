package com.qiusuo.videoeditor.ui.adapter.idocode;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.RecyclerView.ViewHolder;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.qiusuo.videoeditor.module.select.adapter.ItemMoveListener;
import com.qiusuo.videoeditor.module.select.adapter.ItemSelectListener;

public class ItemDragHelperCallback extends ItemTouchHelper.Callback {
	private MoveFilter mFilter;
	private boolean isLongPressDragEnabled = true;
	private int mDragFlags;

	public ItemDragHelperCallback(MoveFilter filter) {
		this.mFilter = filter;
	}

	public void setDragFlags(int dragFlags) {
		this.mDragFlags = dragFlags;
	}

	@Override
	public int getMovementFlags(RecyclerView recyclerView, ViewHolder viewHolder) {
		if (mFilter != null && !mFilter.canMove(viewHolder.getAdapterPosition())) {
			return 0;
		}
		int dragFlags;
		if (mDragFlags != 0) {
			dragFlags = mDragFlags;
		} else {
			RecyclerView.LayoutManager manager = recyclerView.getLayoutManager();
			if (manager instanceof GridLayoutManager || manager instanceof StaggeredGridLayoutManager) {
				dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN | ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
			} else {
				dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN| ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
			}
		}
		// 如果想支持滑动(删除)操作, swipeFlags = ItemTouchHelper.START | ItemTouchHelper.END
		int swipeFlags = 0;
		return makeMovementFlags(dragFlags, swipeFlags);
	}

	@Override
	public boolean onMove(RecyclerView recyclerView, ViewHolder viewHolder, ViewHolder target) {
		if (viewHolder.getItemViewType() != target.getItemViewType()) {
			return false;
		}
		if (recyclerView.getAdapter() instanceof ItemMoveListener) {
			ItemMoveListener listener = ((ItemMoveListener) recyclerView.getAdapter());
			listener.onItemMove(viewHolder.getAdapterPosition(), target.getAdapterPosition());
		}
		return true;
	}

	@Override
	public void onMoved(RecyclerView arg0, ViewHolder viewHolder, int arg2, ViewHolder target, int arg4, int arg5,
	                    int arg6) {
		arg0.getAdapter().notifyItemMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
		super.onMoved(arg0, viewHolder, arg2, target, arg4, arg5, arg6);
		if (viewHolder instanceof ItemMoveListener){
			ItemMoveListener itemMoveHolder = (ItemMoveListener) viewHolder;
			itemMoveHolder.onItemMove(arg2,arg4);
		}
	}

	@Override
	public void onSwiped(ViewHolder viewHolder, int direction) {
	
	}

	@Override
	public void onSelectedChanged(ViewHolder viewHolder, int actionState) {
		// 不在闲置状态
		if (actionState != ItemTouchHelper.ACTION_STATE_IDLE) {
			if (viewHolder instanceof ItemSelectListener) {
				ItemSelectListener itemViewHolder = (ItemSelectListener) viewHolder;
				itemViewHolder.onItemSelected();
			}
		}
		super.onSelectedChanged(viewHolder, actionState);
	}

	@Override
	public void clearView(RecyclerView recyclerView, ViewHolder viewHolder) {
		if (viewHolder instanceof ItemSelectListener) {
			ItemSelectListener itemViewHolder = (ItemSelectListener) viewHolder;
			itemViewHolder.onItemFinish();
		}
		super.clearView(recyclerView, viewHolder);
	}

	@Override
	public boolean isLongPressDragEnabled() {
		// 不需要长按拖拽功能 我们手动控制
		return isLongPressDragEnabled;
	}

	public void setLongPressDragEnabled(boolean isLongPressDragEnabled) {
		this.isLongPressDragEnabled = isLongPressDragEnabled;
	}

	@Override
	public boolean isItemViewSwipeEnabled() {
		// 不需要滑动功能
		return false;
	}

	public static interface MoveFilter {
		boolean canMove(int position);
		
	}
}