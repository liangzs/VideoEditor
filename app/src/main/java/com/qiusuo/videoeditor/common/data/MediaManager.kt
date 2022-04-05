package com.qiusuo.videoeditor.common.data

import android.content.Context
import android.provider.MediaStore
import android.text.TextUtils
import com.qiusuo.videoeditor.common.bean.MediaFolder
import java.io.File
import java.util.*
import kotlin.collections.ArrayList


class MediaManager {
    var projection = arrayOf(MediaStore.Images.ImageColumns.DATA,
        MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME)

    fun queryFolder(context:Context) : List<MediaFolder>{
        val sortOrder = MediaStore.Images.Media.DATE_TAKEN + " DESC "


        val cursor= context.contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,projection,
        null,null,sortOrder)
        while (cursor!!.moveToNext()){

        }
        //一个辅助集合，防止同一目录被扫描多次
        val dirPaths: HashSet<Any?> = HashSet<Any?>()
        val picList :ArrayList<MediaFolder> = ArrayList()

        while (cursor.moveToNext()) {
            // 获取图片的路径
            val path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA))
            val bucketName =
                cursor.getString(cursor.getColumnIndex(MediaStore.Images.ImageColumns.BUCKET_DISPLAY_NAME))
//            if (TextUtils.isEmpty(allFolderItem.coverImagePath)) {
//                allFolderItem.coverImagePath = path
//            }
            val parentFile = File(path).parentFile ?: continue
            val dirPath = parentFile.absolutePath
            var folderItem: MediaFolder? = null
            // 利用一个HashSet防止多次扫描同一个文件夹（不加这个判断，图片多起来还是相当恐怖的~~）
            if (dirPaths.contains(dirPath)) {
                continue
            } else {
                dirPaths.add(dirPath)
                var isNew = true
                //判断一下是否dirPath不同，但是bucketName相同
                run{
                    picList.forEach { item ->
                        if (item.bucketName.equals(bucketName)) {
                            folderItem = item
                            item.parentPath=dirPath
                            isNew = false
                            return@run
                        }
                    }
                }
                if (isNew) {
                    folderItem = MediaFolder();
                    folderItem!!.coverImagePath = path
                    folderItem!!.bucketName = bucketName
                    folderItem!!.parentPath=dirPath
                }
            }
            val array = parentFile.list { dir, filename ->
                if (filename.endsWith(".jpg") || filename.endsWith(".png") || filename.endsWith(".jpeg")) true else false
            }
            val arrayCount = array?.size ?: 0
            folderItem!!.count += arrayCount
            if (!picList.contains(folderItem) && arrayCount > 0) {
                picList.add(folderItem!!)
            }
        }
        return emptyList();
    }


}