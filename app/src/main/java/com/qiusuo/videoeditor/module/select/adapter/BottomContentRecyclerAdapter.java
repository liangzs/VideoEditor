package com.qiusuo.videoeditor.module.select.adapter;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.ijoysoft.mediasdk.module.entity.PhotoMediaItem;
import com.qiusuo.videoeditor.R;
import com.qiusuo.videoeditor.common.bean.MediaEntity;
import com.qiusuo.videoeditor.common.bean.MediaItemPicker;
import com.qiusuo.videoeditor.common.data.MediaDataRepository;
import com.qiusuo.videoeditor.util.TimeUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wh on 2019/4/29.
 * 底部照片选择适配器
 */
public class BottomContentRecyclerAdapter extends RecyclerView.Adapter<BottomContentRecyclerAdapter.MyViewHolder> {

    /**
     * TODO:有多线程操作，可能会引发线程不安全问题
     */
    private List<MediaItem> mDatas;

    /**
     * 根据id 获取media在 {@link BottomContentRecyclerAdapter#mDatas} 中的位置
     * 其中id 的integer， 从左往右第一位用于标记video = 1 或 photo = 0
     * 如：
     * Integer.MIN_VALUE & 1 表示 id为1的视频 二进制: 1000 0000 0000 0000 0000 0000 0000 0001
     * 1 表示id为1的照片 二进制:  0000 0000 0000 0000 0000 0000 0000 0001
     * TODO:有多线程操作，可能会引发线程不安全问题
     */
    private Map<Integer, Integer> idToIndex;

    private Context mContext;
    private int photoCount;
    private int videoCount;
    private ItemClickListener mItemClickListener;

    public BottomContentRecyclerAdapter(Context context, List<MediaItem> mediaItems) {

        mContext = context;
        mDatas = new ArrayList<>();
        mDatas.addAll(mediaItems);
        idToIndex = new HashMap<>();
        for (int i = 0; i < mDatas.size(); i++) {
            MediaItem mediaItem = mDatas.get(i);
            idToIndex.put(getIdMapKey(mediaItem), i);
            updateMediaItemIndex(mediaItem);
        }
        resetCount();
    }

    /**
     * addClip模式下原来有的数量
     */
    private int originSize = 0;

    private void resetCount() {
        photoCount = 0;
        videoCount = 0;
        for (MediaItem m : mDatas) {
            if (m.isImage()) {
                photoCount++;
            } else {
                videoCount++;
            }
        }
    }

    public void inCrease(boolean isImage) {
        if (isImage) {
            photoCount++;
        } else {
            videoCount++;
        }
    }

    /**
     * 注意 mediaItems 不能为空，若为空请使用clear()
     * @param mediaItems isNotEmpty or call clear() when it is empty
     */
    public void setDatas(List<MediaItem> mediaItems) {
        if (ObjectUtils.isEmpty(mediaItems)) {
            return;
        }
        Map<Integer, Integer> idToIndexOld;
        List<MediaItem> mdatasOld;
        if (mDatas != null && mDatas.size() > 0) {
//            for (MediaItem mediaItem : mDatas) {
//               updateMediaItemIndex(mediaItem);
//            }
            mdatasOld = new ArrayList<>(mDatas);
            mDatas.clear();
            idToIndexOld = new HashMap<>(idToIndex);
            idToIndex.clear();
        } else {
            idToIndexOld = new HashMap<>();
            mdatasOld = new ArrayList<>();
        }
        mDatas.addAll(mediaItems);
        idToIndex = new HashMap<>();
        for (int i = 0; i < mDatas.size(); i++) {
            MediaItem mediaItem = mDatas.get(i);
            idToIndex.put(getIdMapKey(mediaItem), i);
        }
        //遍历寻找修改过的item
        for (Integer id : idToIndex.keySet()) {
            Integer indexOld = idToIndexOld.get(id);
            Integer index = idToIndex.get(id);
            //顺序位置和裁剪信息不相同，更新
            if (!index.equals(indexOld) && indexOld == null
                    || !ObjectUtils.equals(mdatasOld.get(indexOld).getVideoCutInterval(), mDatas.get(index).getVideoCutInterval())
            ) {
                //更新位置变化的item
                updateMediaItemIndex(mDatas.get(idToIndex.get(id)));
            }
            idToIndexOld.remove(id);
        }
        //更新被删除的item
        for (Integer id : idToIndexOld.keySet()) {
            int index = idToIndexOld.get(id);
            updateMediaItemIndex(mdatasOld.get(index));
        }
        idToIndexOld.clear();
        mdatasOld.clear();
        resetCount();
        notifyDataSetChanged();
    }

    /**
     * 补充数据
     */
    public void appendData(List<MediaItem> list) {
        for (MediaItem mediaItem : list) {
            if (!mDatas.contains(mediaItem)) {
                mDatas.add(mediaItem);
            }
        }
        idToIndex = new HashMap<>();
        for (int i = 0; i < mDatas.size(); i++) {
            MediaItem mediaItem = mDatas.get(i);
            idToIndex.put(getIdMapKey(mediaItem), i);
            updateMediaItemIndex(mediaItem);
            inCrease(mediaItem.isImage());
        }
        notifyDataSetChanged();
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }


    public interface ItemClickListener {
        void onItemClick(View view, int position);

        void onItemLongClick();
    }

    public void setMoveOutBoundsListener(MoveOutBoundsListener moveOutBoundsListener) {
        mMoveOutBoundsListener = moveOutBoundsListener;
    }

    private MoveOutBoundsListener mMoveOutBoundsListener;

    public interface MoveOutBoundsListener {
        void outBounds();
    }


    public List<MediaItem> getDatas() {
        return mDatas;
    }


    public void clear() {
        if (!ObjectUtils.isEmpty(mDatas)) {
            for (MediaItem mediaItem : mDatas) {
                updateMediaItemIndex(mediaItem);
            }
            mDatas.clear();
            videoCount = 0;
            photoCount = 0;
            idToIndex.clear();
        }

        notifyDataSetChanged();
    }

    public int getPhotoCount() {
        return photoCount;
    }

    public void setPhotoCount(int photoCount) {
        this.photoCount = photoCount;
    }

    public int getVideoCount() {
        return videoCount;
    }

    public void setVideoCount(int videoCount) {
        this.videoCount = videoCount;
    }

    //只加数据，不通知更新
    public MediaItem remove(final View view, final int position) {
        if (position == 0 && mDatas.size() > 2) {
            mDatas.get(1).setTransitionFilter(null);
        }
        if (position >= mDatas.size()) {
            return null;
        }
        if (mDatas.get(position).isImage()) {
            photoCount--;
        } else {
            videoCount--;
        }
        MediaItem mediaItem = mDatas.remove(position);
        idToIndex.remove(getIdMapKey(mediaItem));
        updateMediaItemIndex(mediaItem);
        for (int i = position; i < mDatas.size(); i++) {
            mediaItem = mDatas.get(i);
            idToIndex.put(getIdMapKey(mediaItem), i);
//            updateMediaItemIndex(mediaItem);
        }
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, getItemCount());
        return mediaItem;
    }

    public void update(MediaItem mediaItem) {
        if (mDatas != null) {
            idToIndex.put(getIdMapKey(mediaItem), mDatas.size());
            mDatas.add(mediaItem);
        }
        ((Activity)mContext).runOnUiThread(()->{
            updateMediaItemIndex(mediaItem);
        });
    }

    /**
     * 包含裁剪数据，更新时间
     *
     * @param mediaEntity entity
     * @return entity是否存在mDatas中
     */
    public boolean flash(MediaEntity mediaEntity) {
        if (mDatas != null) {
            Integer index = idToIndex.get(mediaEntity.getId() | Integer.MIN_VALUE);
            if (index == null) {
                return false;
            }
            MediaItem mediaItem = mDatas.get(index);
//            mediaEntity.setVideoTrimCuttedToMediaItem(mediaItem);
            notifyItemChanged(index);
            updateMediaItemIndex(mediaItem);
            return true;
        }
        return false;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_bottem_content, viewGroup, false);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final MyViewHolder myViewHolder, final int i) {
//        LogUtils.d(getClass().getSimpleName() + "#onBindViewHolder_step2", "id:" + mDatas.get(i));
        if (ObjectUtils.isEmpty(mDatas) || ObjectUtils.isEmpty(mDatas.get(i))) {
            return;
        }
        if (mDatas.get(i).isVideo()) {
            String path = ObjectUtils.isEmpty(mDatas.get(i).getTrimPath()) ? mDatas.get(i).getPath() : mDatas.get(i).getTrimPath();
            Glide.with(mContext).load(path).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .override(80, 80).into(myViewHolder.selectImg);
            long duration = mDatas.get(i).getFinalDuration() != 0 ? mDatas.get(i).getFinalDuration() : mDatas.get(i).getDuration();
            if (mDatas.get(i).getDuration() < 1000) {
                myViewHolder.mDurationText.setText(TimeUtils.format(1000, TimeUtils.Min_SEC_PATTER));
            } else if (duration > 3600000) {
                myViewHolder.mDurationText.setText(TimeUtils.calculateTimeOnlyMinuteAndSecond(duration));
            } else {
                myViewHolder.mDurationText.setText(TimeUtils.format(duration, TimeUtils.Min_SEC_PATTER));
            }
        } else {
            myViewHolder.mDurationText.setText("");
            Glide.with(mContext).load(mDatas.get(i)
                            .getPath()).diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(new BitmapDrawable(PhotoMediaItem.getDefaultBitmap()))
                    .override(80, 80).into(myViewHolder.selectImg);
        }
        myViewHolder.deleteImage.setImageResource(R.drawable.vector_delete);
        myViewHolder.deleteImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mItemClickListener != null && myViewHolder.getAdapterPosition() != -1) {
                    mItemClickListener.onItemClick(myViewHolder.itemView, myViewHolder.getAdapterPosition());
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }

    private boolean isNotOutOfBounds(int position) {
        return position < getItemCount() && position > -1;
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements ItemSelectListener, ItemMoveListener {
        ImageView selectImg;
        ImageView deleteImage;
        TextView mDurationText;
        View itemView;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            this.itemView = itemView;
            selectImg = itemView.findViewById(R.id.image_select);
            deleteImage = itemView.findViewById(R.id.delete_img);
            mDurationText = itemView.findViewById(R.id.bottom_duration);
            selectImg.setOnClickListener(this::onClick);
        }

        @Override
        public void onItemSelected() {
            if (mItemClickListener != null) {
                mItemClickListener.onItemLongClick();
            }
            AnimatorSet animatorSet = new AnimatorSet();
            ObjectAnimator ahlha = ObjectAnimator.ofFloat(itemView, "alpha", 1f, 0.5f);
//			ObjectAnimator scaleY = ObjectAnimator.ofFloat(itemView, "scaleY", 1f, 1.5f);
            animatorSet.setDuration(300);
            animatorSet.setInterpolator(new DecelerateInterpolator());
            animatorSet.play(ahlha);
            animatorSet.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    itemView.setAlpha(0.5f);
                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }
            });
            animatorSet.start();
        }

        @Override
        public void onItemFinish() {
            itemView.setAlpha(1f);
            notifyDataSetChanged();
        }

        private void exchangeMediaItem(MediaItem m1, MediaItem m2) {
            List<Bitmap> bitmapList = m1.getMimapBitmaps();
            m1.setMimapBitmaps(m2.getMimapBitmaps());
            m2.setMimapBitmaps(bitmapList);
        }

        @Override
        public void onItemMove(int fromPosition, int toPosition) {
            if (fromPosition + originSize >= MediaDataRepository.getInstance().getDataOperate().size() || toPosition + originSize >= MediaDataRepository.getInstance().getDataOperate().size()) {
                if (mMoveOutBoundsListener != null) {
                    mMoveOutBoundsListener.outBounds();
                }
                return;
            }
            if (isNotOutOfBounds(fromPosition) && isNotOutOfBounds(toPosition)) {
                if (fromPosition < toPosition) {
                    for (int i = fromPosition; i < toPosition; i++) {
                        Collections.swap(mDatas, i, i + 1);
                        idToIndex.put(getIdMapKey(mDatas.get(i)), i);
                        idToIndex.put(getIdMapKey(mDatas.get(i + 1)), i + 1);
                    }
                } else {
                    for (int i = fromPosition; i > toPosition; i--) {
                        Collections.swap(mDatas, i, i - 1);
                        idToIndex.put(getIdMapKey(mDatas.get(i)), i);
                        idToIndex.put(getIdMapKey(mDatas.get(i - 1)), i - 1);
                    }
                }
                //MediaDataRepository中已做了转场mipmap的交换了
                MediaDataRepository.getInstance().swapOperate(fromPosition + originSize, toPosition + originSize);
            }
        }

        public void onClick(View v) {
            if (v.getId() == R.id.image_select) {
                //临时MediaEntity，仅供预览使用，切勿作他用
                MediaEntity mediaEntity = new MediaEntity();
                int index = getAdapterPosition();
                if (index < 0) {
                    return;
                }
                MediaItem mediaItem = mDatas.get(index);
                mediaEntity.setId(mediaItem.getId());
                mediaEntity.setType(mediaItem.getMediaType());
                mediaEntity.setPath(mediaItem.getPath());
//                mediaEntity.setDuration(mediaItem.getDuration());
//                mediaEntity.setOriginDuration(mediaItem.getVideoOriginDuration());
                mediaEntity.setHeight(mediaItem.getHeight());
                mediaEntity.setWidth(mediaItem.getWidth());
                mediaEntity.setRotation(mediaItem.getRotation());
//                mediaEntity.setVideoTrimCuttedValue(mediaItem);
                if (mediaEntity.type == MediaEntity.TYPE_VIDEO) {
                    MediaDataRepository.getInstance().setTempMediaEntity(mediaEntity);
                    MediaDataRepository.getInstance().getTempMediaEntity().loadRotation();
                }
//                AppBus.get().post(new PreviewEvent(mediaEntity, true, index));
            }
        }

        public ImageView getDeleteImage() {
            return deleteImage;
        }

        public ImageView getSelectImg() {
            return selectImg;
        }

        /**
         * 显示与隐藏灰色前景
         *
         * @param visiable
         */
        public void setDurationTextVisibility(boolean visiable) {
            if (visiable) {
                mDurationText.setVisibility(View.VISIBLE);
            } else {
                mDurationText.setVisibility(View.GONE);
            }
        }
    }


    public Map<Integer, Integer> getIdToIndex() {
        return idToIndex;
    }

    /**
     * 更新素材右上角位置顺序标记
     *
     * @param mediaItem 媒体素材
     */
    private void updateMediaItemIndex(MediaItem mediaItem) {
        if (mContext instanceof MediaItemPicker) {
            ((Activity)mContext).runOnUiThread(()->{
                LogUtils.d(getClass().getSimpleName() + "#updateMediaItemIndex", "media id:" + mediaItem.getId() + " mediaType:" + mediaItem.getMediaType().getDetail());
                if (mediaItem.getMediaType() == MediaType.PHOTO && mediaItem.getId() < 0) {
                    ((MediaItemPicker) mContext).updateCopiedMediaItemSelected(mediaItem.getPath());
                } else {
                    ((MediaItemPicker) mContext).updateMediaItemIndex(mediaItem);
                }
            });
        }
    }


    /**
     * 根据不同类型的媒体素材，获取其在idToIndex Map中的key
     *
     * @param mediaItem 媒体素材
     * @return photo: id, video: id & Integer.MIN_VALUE
     */
    public static int getIdMapKey(MediaItem mediaItem) {
        return getIdMapKey(mediaItem.getMediaType(), mediaItem.getId());
    }

    public static int getIdMapKey(MediaType mediaType, int id) {
        if (mediaType == MediaType.VIDEO) {
            id = id | Integer.MIN_VALUE;
        }
        return id;
    }

    /**
     * 某个路径的位置
     */
    public int indexOfPath(String path) {
        int i = 0;
        for (MediaItem mediaItem : mDatas) {
            if (mediaItem.getPath().equals(path)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    public void setOriginSize(int originSize) {
        this.originSize = originSize;
    }
}
