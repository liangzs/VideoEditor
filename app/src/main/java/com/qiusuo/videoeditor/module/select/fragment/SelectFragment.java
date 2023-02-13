package com.qiusuo.videoeditor.module.select.fragment;

public interface SelectFragment {

    void updateData();

    void switchAdapter(int position);

    void showSelect();

    void updateSelect();

    void updateCopiedSelectedItemIndex(String path);

    void updateDateCount(String date, int mediaType, boolean increase);

    void clearSelectCount();

    void updateSections();

    void recountSections();
}
