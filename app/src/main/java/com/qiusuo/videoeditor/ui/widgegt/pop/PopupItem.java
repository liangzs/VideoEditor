package com.qiusuo.videoeditor.ui.widgegt.pop;

import android.content.Context;

public class PopupItem {
    private int textId;
    private int iconId;
    private boolean mSubMenu;
    private boolean mTitleMenu;
    private boolean mTitleIconMenu;
    private boolean mCheckMenu;
 private boolean mSelected;
    private String mCustomText;
    public PopupItem() {
    }

    public int getTextId() {
        return textId;
    }

    public int getIconId() {
        return iconId;
    }

    public boolean isSubMenu() {
        return mSubMenu;
    }

    public boolean isTitleMenu() {
        return mTitleMenu;
    }

    public boolean isTitleIconMenu() {
        return mTitleIconMenu;
    }

    public boolean isCheckMenu() {
        return mCheckMenu;
    }
    
    public boolean isSelected() {
        return this.mSelected;
    }
    
    
    public PopupItem setCustomText(String customText) {
        this.mCustomText = customText;
        return this;
    }
    
    public String getText(Context context) {
        return this.mCustomText != null ? this.mCustomText : context.getString(this.textId);
    }

    public static PopupItem create(int textId) {
        PopupItem popupItem = new PopupItem();
        popupItem.textId = textId;
        popupItem.mTitleIconMenu = false;
        return popupItem;
    }
    
    public static PopupItem create(int textId, boolean selected) {
        PopupItem popupItem = create(textId);
        popupItem.mSelected = selected;
        return popupItem;
    }

    

    public static PopupItem createSubMenu(int textId) {
        PopupItem popupItem = create(textId);
        popupItem.mSubMenu = true;
        return popupItem;
    }

    public static PopupItem createTitleMenu(int textId) {
        PopupItem popupItem = create(textId);
        popupItem.mTitleMenu = true;
        return popupItem;
    }

    public static PopupItem createTitleMenu(int textId, int iconId) {
        PopupItem popupItem = new PopupItem();
        popupItem.textId = textId;
        popupItem.iconId = iconId;
        popupItem.mTitleIconMenu = true;
        return popupItem;
    }

    public static PopupItem createCheckMenu(int textId) {
        PopupItem popupItem = create(textId);
        popupItem.mCheckMenu = true;
        return popupItem;
    }
}
