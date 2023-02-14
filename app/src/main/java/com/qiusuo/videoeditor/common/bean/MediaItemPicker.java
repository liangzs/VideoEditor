package com.qiusuo.videoeditor.common.bean;

import com.ijoysoft.mediasdk.module.entity.DurationInterval;
import com.ijoysoft.mediasdk.module.entity.MediaItem;
import com.ijoysoft.mediasdk.module.entity.MediaType;

public interface MediaItemPicker {

    /**
     * 更新fragment中mediaItem的顺序
     *
     * @param mediaId   mediaId
     * @param mediaType mediaType
     */
    void updateMediaItemIndex(int mediaId, MediaType mediaType);


    /**
     * 更新fragment中mediaItem的顺序
     *
     * @param mediaItem
     */
    void updateMediaItemIndex(MediaItem mediaItem);

    /**
     * 获取素材在选中片段中的顺序位置
     *
     * @param mediaType
     * @param id
     * @return null 表示不在选择集合中, 返回下标位置
     */
    Integer getMediaItemSelectedIndex(MediaType mediaType, int id);

    /**
     * 获取素材在选中片段中的顺序位置
     *
     * @param mediaItem 元素
     * @return null 表示不在选择集合中, 返回下标位置
     */
    Integer getMediaItemSelectedIndex(MediaEntity mediaItem);


    /**
     * 获取裁剪duration
     *
     * @param mediaItem
     * @return
     */
    DurationInterval getMediaItemCutDuration(MediaEntity mediaItem);

    /**
     * 获取裁剪的path
     *
     * @param mediaEntity
     * @return
     */
    String getMediaItemCutPath(MediaEntity mediaEntity);


    /**
     * 更新复制模式下的选择
     */
    default void updateCopiedMediaItemSelected(String path) {
    }
}
