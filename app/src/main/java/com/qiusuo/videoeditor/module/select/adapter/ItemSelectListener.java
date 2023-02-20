package com.qiusuo.videoeditor.module.select.adapter;

public interface ItemSelectListener {
	/**
	 * 拖动 刚选中时
	 */
	void onItemSelected();
	/**
	 * 拖动 放开结束时
	 */
	void onItemFinish();
}
