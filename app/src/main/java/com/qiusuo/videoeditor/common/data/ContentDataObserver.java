package com.qiusuo.videoeditor.common.data;

import android.content.Context;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;


/**
 * Created by DELL on 2019/7/30.
 */
public class ContentDataObserver extends ContentObserver {


    /**
     * Creates a content observer.
     *
     * @param handler The handler to run {@link #onChange} on, or null if none.
     */
    public ContentDataObserver(Context context, Handler handler) {
        super(handler);
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);
    }

    @Override
    public void onChange(boolean selfChange, Uri uri) {
        super.onChange(selfChange, uri);
        if (mOnUriChange != null) {
            mOnUriChange.onChanged(uri);
        }
    }

    public void setOnUriChange(onUriChange onUriChange) {
        mOnUriChange = onUriChange;
    }

    private onUriChange mOnUriChange;

    public interface onUriChange {
        void onChanged(Uri uri);
    }
}
