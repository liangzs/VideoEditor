package com.qiusuo.videoeditor.common.constant

class RequestCode {
    companion object {
        const val SELECT = "select"
        const val SECTIONS = "sections"
        const val DRAFT_SELECT_MODE = "draft_select_mode"
        const val WORK_SELECT_MODE = "work_select_mode"
        const val ADD_CLIP = "add_clip"
        const val DRAFT_EDIT_TO_CLIP = "draft_edit_to_clip"
        const val ANDROID_R_TRANSITION = "android_r_transition"

        const val SHOW_ORIGIN_ITEM = "show_origin_item"

        const val IS_DELAY = "is_delay"

        const val SHOW_LOAD = "show_load"
        const val REMOVE_VIDEO_SOUND = "remove_video_sound" //移除视频原音字段

        const val TO_DRAFT_AND_WORK = "to_draft_and_work"
        const val EDIT_TRIM = "edit_trim"
        const val SHARE_TIMES = "share_times"
        const val MURGE_TIMES = "murge_times"

        /**
         * 指定贴纸类型的tag
         */
        const val STICKER_GROUP = "group"

        /**
         * 指定字体
         */
        const val FONT_ENTITY = "fontEntity"


        const val REQUEST_ADD_TEXT = 0x00
        const val REQUEST_ADD_MUSIC = REQUEST_ADD_TEXT + 1
        const val REQUEST_ADD_FILTER = REQUEST_ADD_MUSIC + 1
        const val REQUEST_ADD_TRANSITION = REQUEST_ADD_FILTER + 1
        const val REQUEST_ADD_DOODLE = REQUEST_ADD_TRANSITION + 1
        const val REQUEST_ADD_MUSIC_SINGLE = REQUEST_ADD_DOODLE + 1
        const val REQUEST_ADD_PICTURE = REQUEST_ADD_MUSIC_SINGLE + 1
        const val REQUEST_EDIT_CLIP = REQUEST_ADD_PICTURE + 1
        const val REQUEST_EDIT_CLIP_CUT_VIDEO = REQUEST_EDIT_CLIP + 1
        const val REQUEST_EDIT_SORT = REQUEST_EDIT_CLIP_CUT_VIDEO + 1
        const val REQUEST_TO_WORK = REQUEST_EDIT_CLIP_CUT_VIDEO + 1
        const val REQUEST_TO_DRAFT = REQUEST_TO_WORK + 1
        const val APP_TYPE_OLD = REQUEST_TO_DRAFT + 1
        const val APP_TYPE_NEW = APP_TYPE_OLD + 1
        const val REQUEST_CHANGE_THEME = APP_TYPE_NEW + 1
        const val REQUEST_CROP = REQUEST_CHANGE_THEME + 1
        const val REQUEST_FULL_SCREEN = REQUEST_CROP + 1
        const val REQUEST_SELECT_TO_EDIT = REQUEST_FULL_SCREEN + 1
        const val REQUEST_ADD_RECORD = REQUEST_SELECT_TO_EDIT + 1
        const val REQUEST_CHANGE_BORDER = REQUEST_SELECT_TO_EDIT + 1
        const val REQUEST_CHANGE_EFFECT = REQUEST_SELECT_TO_EDIT + 1

        const val REQUEST_CAMERA_PREMISSION = 3000
    }
}