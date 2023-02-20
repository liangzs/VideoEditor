package com.qiusuo.videoeditor.module.select.adapter;

public interface ItemMoveListener {
	/**
	 * 拖动 两个item改变位置时
	 * @param position1 item1位置
	 * @param position2 item2位置
	 */
	void onItemMove(int position1, int position2);
}
