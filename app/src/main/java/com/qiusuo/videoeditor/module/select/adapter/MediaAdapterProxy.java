package com.qiusuo.videoeditor.module.select.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.ijoysoft.mediasdk.R;
import com.ijoysoft.mediasdk.common.utils.LogUtils;
import com.ijoysoft.mediasdk.common.utils.ObjectUtils;
import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;
import com.qiusuo.videoeditor.common.bean.MediaEntity;
import com.qiusuo.videoeditor.common.bean.MediaItemPicker;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wh on 2019/4/29.
 */
public class MediaAdapterProxy {
    protected Context mContext;
    protected Map<String, List<MediaEntity>> mHashMap = new HashMap<>();
    protected List<String> mDates = new ArrayList<>();

    /**
     * 当前section前面其他Section的item总数
     */
    protected int[] prevSectionCount;

    /**
     * 选择计数
     */
    protected Map<String, Integer> selectCount = new HashMap<>();
    /**
     * 原生Adapter使用的
     */
    List<MediaEntity> nativeData = new ArrayList<>();
    Map<Integer, Integer> idToIndex = new HashMap<>();

    public MediaAdapterProxy(Context context, List<String> dates, Map<String, List<MediaEntity>> hashMap, List<MediaEntity> nativeData) {
        mContext = context;
        setData(dates, hashMap, nativeData);
    }

    /**
     * 是否为添加模式
     */
    boolean isAddClip = false;


    /**
     * 设置成功返回true
     *
     * @param dates   文件夹
     * @param hashMap 数据源
     * @return true 设置成功
     * @return false 数据不一致，不设置
     */
    public boolean setData(List<String> dates, Map<String, List<MediaEntity>> hashMap, List<MediaEntity> nativeData) {
        if (nativeData.isEmpty()) {
            this.nativeData.clear();
            mHashMap.clear();
            mDates.clear();
            return false;
        }
//        LogUtils.i("updateData", "dates:" + dates.size() + ",hashMap:" + hashMap.size() + "refresh:" + refresh);
        if (dates.size() == hashMap.size()) {
            mHashMap.clear();
            mDates.clear();
            prevSectionCount = new int[dates.size()];
            int index = 0;
            int count = 0;
            ArrayList<String> listSyn = (ArrayList<String>) ((ArrayList) dates).clone();
            for (String date : listSyn) {
                List<MediaEntity> list = new ArrayList<>(hashMap.get(date));
                prevSectionCount[index] = count;
                count += list.size();
                index++;
                mHashMap.put(date, list);
            }
            this.nativeData = new ArrayList<>(nativeData);
            mDates.addAll(dates);
            selectCount.clear();
            return true;
        } else {
            return false;
        }
    }

    /**
     * 检查全选按钮是否该选中
     */
    public boolean isSectionChecked(int sectionIndex, int[] sectionForPosition) {
        String section = mDates.get(sectionIndex);
        List<MediaEntity> mediaEntities = mHashMap.get(section);
        if (selectCount.get(section) == null) {
            int startIndex = 0;
            //查找数组起始位置
            for (String date : mDates) {
                if (date.equals(mDates.get(sectionIndex))) {
                    break;
                }
                startIndex += 1 + mHashMap.get(date).size();
            }
            //header 占用了一个位置
            startIndex++;
            int count = 0;
            int i = 0;
            while (i < mediaEntities.size()) {
                if (((MediaItemPicker) mContext).getMediaItemSelectedIndex(mediaEntities.get(i++)) != null) {
                    count++;
                }
                startIndex++;
            }
            selectCount.put(section, count);
            LogUtils.v("selectCount", "inner change" + section + "count:" + count);
        }
        return selectCount.get(section) == mediaEntities.size();
    }

    public MediaEntity getMediaItem(int section, int posion) {
        if (section == -1) {
            final List<MediaEntity> mediaItems = mHashMap.get(mDates.get(section));
            if (mediaItems == null || mediaItems.isEmpty()) {
                return new MediaEntity();
            }
            return mediaItems.get(posion);
        } else {
            return nativeData.get(posion);
        }
    }

    public void setOnItemClickListener(onItemClickListener onItemClickListener) {
        mOnItemClickListener = onItemClickListener;
    }

    public onItemClickListener getOnItemClickListener() {
        return mOnItemClickListener;
    }

    onItemClickListener mOnItemClickListener;

    public interface onItemClickListener {

        void onItemSelected(View view, MediaEntity mediaItem);

        void onItemClick(View view, MediaEntity mediaItem, int sourceIndex);

        default void selectAll(String selectDate, boolean toSelect) {
        }

    }

    public void clear() {
        for (int i = 0; i < mDates.size(); i++) {
            List<MediaEntity> arrayList = mHashMap.get(mDates.get(i));
            for (int j = 0; j < arrayList.size(); j++) {
                arrayList.get(j).isSelected = false;
            }
        }
    }

    public String getSectionHeaderTitle(int section) {
        return mDates.get(section);
    }

    public int getSectionCount() {
        return mDates == null ? 0 : mDates.size();
    }

    protected int getItemCountForSection(int section) {
        List<MediaEntity> mediaItems = mHashMap.get(mDates.get(section));
        if (ObjectUtils.isEmpty(mediaItems)) {
            return 0;
        }
        return mediaItems.size();
    }

    protected MyViewHolder onCreateItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.item_fragment_media_layout, parent, false);
        MyViewHolder holder = new MyViewHolder(view);
        return holder;
    }

    protected void onBindItemViewHolder(MyViewHolder holder, final int section, final int position) {
        holder.section = section;
        holder.positionInSection = position;
        MediaEntity mediaEntity;
        if (section == -1) {
            mediaEntity = nativeData.get(position);
        } else {
            final List<MediaEntity> mediaItems = mHashMap.get(mDates.get(section));
            mediaEntity = mediaItems.get(position);
        }
        holder.mediaEntity = mediaEntity;
        Integer index = null;
        if (mContext instanceof MediaItemPicker) {
            index = ((MediaItemPicker) mContext).getMediaItemSelectedIndex(mediaEntity);
        }
        String path;
        if (mediaEntity.type == MediaEntity.TYPE_VIDEO) {
            int duration;
            DurationInterval durationInterval;
            if (index == null) {
                durationInterval = mediaEntity.getVideoCutInterval();
                if (durationInterval == null) {
                    duration = (int) mediaEntity.getDuration();
                    path = mediaEntity.getPath();
                    holder.trimLabel.setImageDrawable(null);
                } else {
                    if (durationInterval.isModeCut()) {
                        duration = (int) (durationInterval.getStartDuration() + mediaEntity.getOriginDuration() - durationInterval.getEndDuration());
                    } else {
                        duration = durationInterval.getInterval();
                    }
                    path = mediaEntity.getTrimPath();
                    holder.trimLabel.setImageResource(R.drawable.vector_trim);
                }
            } else {
                durationInterval = ((MediaItemPicker) mContext).getMediaItemCutDuration(mediaEntity);
                if (durationInterval == null) {
                    duration = (int) mediaEntity.getOriginDuration();
                    path = mediaEntity.getOriginPath();
                    holder.trimLabel.setImageDrawable(null);
                } else {
                    if (durationInterval.isModeCut()) {
                        duration = (int) (durationInterval.getStartDuration() + mediaEntity.getOriginDuration() - durationInterval.getEndDuration());
                    } else {
                        duration = durationInterval.getInterval();
                    }
                    path = ((MediaItemPicker) mContext).getMediaItemCutPath(mediaEntity);
                    holder.trimLabel.setImageResource(R.drawable.vector_trim);
                }
                mediaEntity.setDuration(duration);
                mediaEntity.setVideoCutInterval(durationInterval);
                mediaEntity.setTrimPath(path);
            }


            if (duration < 1000) {
                holder.mDurationText.setText(TimeUtils.format(1000, TimeUtils.Min_SEC_PATTER));
            } else if (duration > 3600 * 1000) {
                holder.mDurationText.setText(TimeUtils.calculateTimeOnlyMinuteAndSecond(duration));
                // holder.mDurationText.setText(TimeUtils.format(duration/1000,TimeUtils.HOUR_MIN_SEC_PATTER));
            } else {
                holder.mDurationText
                        .setText(TimeUtils.format(duration, TimeUtils.Min_SEC_PATTER));
            }
        } else {
            path = mediaEntity.getPath();
            holder.mDurationText.setText("");
        }
        Glide.with(mContext).load(path)
                .centerCrop()
                .override(200, 200)
                .into(holder.mSquareImg);

        holder.mSelectButtonSpace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FileUtil.exists(mediaEntity.path) && !mediaEntity.path.startsWith("http")) {
                    T.showShort(mContext, mContext.getString(R.string.file_not_exist));
                    return;
                }
                if (mOnItemClickListener != null) {
                    MediaEntity mediaItem = mediaEntity;
                    mOnItemClickListener.onItemSelected(v, mediaItem);
                }
            }
        });

        holder.mSquareImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!FileUtil.exists(mediaEntity.path) && !mediaEntity.path.startsWith("http")) {
                    T.showShort(mContext, mContext.getString(R.string.file_not_exist));
                    return;
                }
                if (mOnItemClickListener != null) {
                    MediaEntity mediaItem = mediaEntity;
                    int start;
                    if (section == -1) {
                        start = 0;
                    } else {
                        start = prevSectionCount[section];
                    }
                    mOnItemClickListener.onItemClick(v, mediaItem, start + holder.positionInSection);
                }
            }
        });

        holder.id = mediaEntity.id;
        switch (mediaEntity.getType()) {
            case MediaEntity.TYPE_VIDEO:
                holder.mediaType = MediaType.VIDEO;
                break;
            case MediaEntity.TYPE_IMAGE:
                holder.mediaType = MediaType.PHOTO;
                break;
            case MediaEntity.TYPE_MUSIC:
                holder.mediaType = MediaType.AUDIO;
                break;
            default:
        }
        //设置选择下标
        if (mContext instanceof SelectPhotoActivity ||
                mContext instanceof VideoTrimActivity &&
                        (
                                ((VideoTrimActivity) mContext).getSelectMode() == VideoTrimActivity.SELECT_MODE_TO_AUDIO
                                        || ((VideoTrimActivity) mContext).getSelectMode() == VideoTrimActivity.SELECT_MODE_COMPRESS)) {
            holder.getSelectButton().setVisibility(View.GONE);
            holder.mSelectButtonSpace.setVisibility(View.GONE);
            holder.mSquareImg.clearColorFilter();
        } else {
            if (index == null) {
                holder.getSelectButton().setBackgroundResource(R.drawable.vector_drawable_item_fragment_media_select);
                holder.getSelectButton().setText("");
                holder.mSquareImg.clearColorFilter();
            } else {
                holder.getSelectButton().setBackgroundResource(R.drawable.vector_drawable_item_fragment_media_selected);
//                    holder.getSelectButton().setText(Integer.toString(index + 1));
                int color;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    color = mContext.getColor(R.color.selected_media_item_activity_theme_alpha);
                } else {
                    color = mContext.getResources().getColor(R.color.selected_media_item_activity_theme_alpha);
                }
                holder.mSquareImg.setColorFilter(color);
            }
        }
        if (isAddClip) {
            boolean contains = false;
            int i = 0;
            for (MediaItem mediaItem : MediaDataRepository.getInstance().getDataOperate()) {
                //不计算新加的
                if (i == ((SelectClipActivity) mContext).getOriginSize()) {
                    break;
                }
                if (mediaEntity.id == mediaItem.getId()) {
                    contains = true;
                    break;
                }
                i++;
            }
            if (contains) {
                holder.imported.setVisibility(View.VISIBLE);
            } else {
                holder.imported.setVisibility(View.GONE);
            }
        } else {
            holder.imported.setVisibility(View.GONE);
        }
    }

    public void updateSelected(MyViewHolder holder) {
        if (mContext instanceof MediaItemPicker) {
            Integer index = null;
            index = ((MediaItemPicker) mContext).getMediaItemSelectedIndex(holder.mediaEntity);
            if (index == null) {
                holder.getSelectButton().setBackgroundResource(R.drawable.vector_drawable_item_fragment_media_select);
                holder.getSelectButton().setText("");
                holder.mSquareImg.clearColorFilter();
            } else {
                holder.getSelectButton().setBackgroundResource(R.drawable.vector_drawable_item_fragment_media_selected);
//                    holder.getSelectButton().setText(Integer.toString(index + 1));
                int color;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    color = mContext.getColor(R.color.selected_media_item_activity_theme_alpha);
                } else {
                    color = mContext.getResources().getColor(R.color.selected_media_item_activity_theme_alpha);
                }
                holder.mSquareImg.setColorFilter(color);
            }
            if (holder.mediaEntity.type == MediaEntity.TYPE_VIDEO) {
                String path;
                int duration;
                DurationInterval durationInterval;
                if (index == null) {
                    durationInterval = holder.mediaEntity.getVideoCutInterval();
                    if (durationInterval == null) {
                        duration = (int) holder.mediaEntity.getDuration();
                        path = holder.mediaEntity.getPath();
                        holder.trimLabel.setImageDrawable(null);
                    } else {
                        if (durationInterval.isModeCut()) {
                            duration = (int) (durationInterval.getStartDuration() + holder.mediaEntity.getOriginDuration() - durationInterval.getEndDuration());
                        } else {
                            duration = durationInterval.getInterval();
                        }
                        path = holder.mediaEntity.getTrimPath();
                        holder.trimLabel.setImageResource(R.drawable.vector_trim);
                    }
                } else {
                    durationInterval = ((MediaItemPicker) mContext).getMediaItemCutDuration(holder.mediaEntity);
                    if (durationInterval == null) {
                        duration = (int) holder.mediaEntity.getOriginDuration();
                        path = holder.mediaEntity.getOriginPath();
                        holder.trimLabel.setImageDrawable(null);
                    } else {
                        duration = durationInterval.getInterval();
                        path = ((MediaItemPicker) mContext).getMediaItemCutPath(holder.mediaEntity);
                        holder.trimLabel.setImageResource(R.drawable.vector_trim);
                    }
                    if (!isAddClip) {
                        holder.mediaEntity.setDuration(duration);
                        holder.mediaEntity.setVideoCutInterval(durationInterval);
                        holder.mediaEntity.setTrimPath(path);
                    }
                }


                if (duration < 1000) {
                    holder.mDurationText.setText(TimeUtils.format(1000, TimeUtils.Min_SEC_PATTER));
                } else if (duration > 3600 * 1000) {
                    holder.mDurationText.setText(TimeUtils.calculateTimeOnlyMinuteAndSecond(duration));
                    // holder.mDurationText.setText(TimeUtils.format(duration/1000,TimeUtils.HOUR_MIN_SEC_PATTER));
                } else {
                    holder.mDurationText
                            .setText(TimeUtils.format(duration, TimeUtils.Min_SEC_PATTER));
                }
            }
        }

    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        MediaEntity mediaEntity;
        SquareImg mSquareImg;
        TextView mDurationText;
        Button mSelectButton;
        int id;
        MediaType mediaType;
        ImageView trimLabel;
        View mSelectButtonSpace;
        TextView imported;
        int section;
        int positionInSection;


        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            mSquareImg = itemView.findViewById(R.id.media_img);
            mDurationText = itemView.findViewById(R.id.duration);
            mSelectButton = itemView.findViewById(R.id.media_item_serial_number_toggle);
            mSelectButton.setClickable(false);
            trimLabel = itemView.findViewById(R.id.trim_label);
            mSelectButtonSpace = itemView.findViewById(R.id.media_item_serial_number_toggle_touch_space);
            imported = itemView.findViewById(R.id.tv_imported);
        }

        public int getId() {
            return id;
        }

        public MediaType getMediaType() {
            return mediaType;
        }

        public Button getSelectButton() {
            return mSelectButton;
        }


        public MediaEntity getMediaEntity() {
            return mediaEntity;
        }
    }

    protected int getLayoutResource() {
        return R.layout.item_time_head_recycler_layout;
    }

    protected int getTitleTextID() {
        return R.id.time_header;
    }

    public boolean isAddClip() {
        return isAddClip;
    }

    public void setAddClip(boolean addClip) {
        isAddClip = addClip;
    }

    public Map<String, Integer> getSelectCount() {
        return selectCount;
    }

}
