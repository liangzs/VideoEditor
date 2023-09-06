package com.qiusuo.videoeditor.common.room

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.io.Serializable
import java.util.Date

/**
 * 序列化时把bitmap对象忽略，反序列重现加载bitmap对象
 */
/**
 * 采用sqlite存储本地数据，子项item
 */
@Entity(tableName = "project")
data class Project(@PrimaryKey val projectId: String,
                   var copyId: String? = null,
                   var createTime: String? = null,
                   var updateTime: String? = null,
                   var duration: Long = 0,
                   var coverPath: String? = null,
                   var projectName: String? = null,
                   var pathName: String? = null,
                   var tranGroup: Int = 0

) {
    constructor() : this("", null, null, null, 0, null, null, null, 0)
}
