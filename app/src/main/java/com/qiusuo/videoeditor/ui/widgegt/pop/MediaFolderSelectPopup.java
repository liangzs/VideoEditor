package com.qiusuo.videoeditor.ui.widgegt.pop;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroupOverlay;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.qiusuo.videoeditor.R;
import com.qiusuo.videoeditor.base.BaseActivity;
import com.qiusuo.videoeditor.common.bean.MediaSet;
import com.qiusuo.videoeditor.common.data.MediaManager;
import com.qiusuo.videoeditor.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MediaFolderSelectPopup extends BasePopupWindow {

    private RecyclerView recyclerView;
    private List<MediaSet> mediaSets;
    private FolderAdapter folderAdapter;
    private int selectFragmtnType;

    @Override
    protected int getLayoutId() {
        return R.layout.media_folder_layout;
    }

    @Override
    public void show(View view) {
        showAtLocation(view, Gravity.TOP, 0, 0);//相对Activity的Window的位置
    }

    public MediaFolderSelectPopup(BaseActivity context, List<MediaSet> list, int selectFragmtnType) {
        super(context);
        mediaSets = new ArrayList<>();
        mediaSets.addAll(list);
        MediaSet mediaSet = MediaManager.INSTANCE.createDefaultMediaSet(selectFragmtnType);
        mediaSets.add(0, mediaSet);
        recyclerView = view.findViewById(R.id.rv_folder);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        folderAdapter = new FolderAdapter();
        recyclerView.setAdapter(folderAdapter);
        setAnimationStyle(R.style.fade_popwindow);
    }


    public void setMediaSets(List<MediaSet> list,int selectFragmtnType) {
        mediaSets = new ArrayList<>();
        mediaSets.addAll(list);
        MediaSet mediaSet = MediaManager.INSTANCE.createDefaultMediaSet(selectFragmtnType);
        mediaSets.add(0, mediaSet);
        folderAdapter.notifyCheckChanged();

    }

    private class FolderAdapter extends RecyclerView.Adapter<FolderHolder> {

        public FolderAdapter() {
        }

        @NonNull
        @Override
        public FolderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new FolderHolder(LayoutInflater.from(activity).inflate(R.layout.media_folder_item_layout, parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull FolderHolder holder, int position) {
            holder.bind(position);
        }


        @Override
        public int getItemCount() {
            return mediaSets.size();
        }

        private void notifyCheckChanged() {
            notifyItemRangeChanged(0, getItemCount(), "check");
        }
    }

    private class FolderHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private MediaSet mediaSet;
        private ImageView imageview;
        private TextView tvTitle;
        private TextView tvCount;

        public FolderHolder(@NonNull View itemView) {
            super(itemView);
            imageview = itemView.findViewById(R.id.iv_folder);
            tvTitle = itemView.findViewById(R.id.tv_folder_name);
            tvCount = itemView.findViewById(R.id.tv_count);
            itemView.setOnClickListener(this::onClick);
        }

        public void bind(int position) {
            mediaSet = mediaSets.get(position);
            LogUtils.i(getClass().getSimpleName(), "path:" + mediaSet.getCoverpath());
            Glide.with(activity).load(mediaSet.getCoverpath())
                    .centerCrop()
//                    .error(R.mipmap.image_error)
                    .into(imageview);
            tvTitle.setText(mediaSet.name);
            tvCount.setText(mediaSet.count + "");
        }


        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mediaSet = mediaSets.get(position);
//            AppBus.get().post(new SelectMediaSetEvent(mediaSet.getBucketId(), mediaSet.getName()));
        }
    }

    @Override
    public void showAsDropDown(View anchor) {
        super.showAsDropDown(anchor);
        ViewGroup parent = (ViewGroup) activity.getWindow().getDecorView().getRootView();
        ViewGroupOverlay overlay = null;
        final float alpha = 0.5f;
        // 下面两个方法对于有些手机会导致surfaceview变黑
        Drawable dim = new ColorDrawable(Color.BLACK);
        dim.setBounds(0, anchor.getHeight() + StatusBarUtil.getStatusBarHeight(anchor.getContext())
                , parent.getWidth(), parent.getHeight());
        dim.setAlpha((int) (255 * alpha));
        overlay = parent.getOverlay();
        overlay.clear();
        overlay.add(dim);
    }

//    @Override
//    public void setHeight(int height) {
//        super.setHeight(height);
//        ViewGroup.LayoutParams lp = view.getLayoutParams();
//        lp.height = height;
//        view.setLayoutParams(lp);
//    }

    /**
     * all文件夹数量+1
     */
    public void addAllCount() {
        mediaSets.get(0).count++;
        folderAdapter.notifyItemChanged(0);
    }


}
