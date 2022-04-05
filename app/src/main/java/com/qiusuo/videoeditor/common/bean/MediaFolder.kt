package com.qiusuo.videoeditor.common.bean

class MediaFolder(var bucketId: Long,
    var bucketName: String,
    var path: String,
    var coverImagePath: String,
    var parentPath: String,
    var count: Int) {
    constructor() : this(0, "", "", "", "",0)
}
