package com.qiusuo.videoeditor.util

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import com.qiusuo.videoeditor.base.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.lang.reflect.Type

/**
 * 存储
 */
object SpUtil {
    private const val NAME = "VideoEdit"

    /*----------------tag----------------------*/
    const val BITMAP_DOODLE_BASE_Y = "bitmap_doodle_base_y"
    const val EDIT_RATIO_SELECT = "edit_ratio_select"
    const val TAG_MUSIC_FADE = "tag_music_fade"
    const val EXPORT_QUALITY = "export_quality"
    const val FORCE_ENGLISH = "force_english"
    const val PHONE_LANGUAGE = "phone_language"
    const val SP_FRAME_TYPE = "sp_frame_type"
    const val SP_RESOLUTION = "sp_resolution"
    const val MEDIAITEM_FIRST_FRAME = "first_frame"

    const val TO_REFREH_WORK = "to_refresh_work"
    const val MURGE_PATH = "murge_path"
    const val CUT_PATH = "cut_path"
    const val ISCUT = "is_cut"
    const val ISSPLITE = "is_splite"
    const val SPLTE_LEFT_PATH = "splite_left_path"
    const val SPLTE_RIGHT_PATH = "splite_right_path"
    const val MUSIC_SORT = "music_sort"
    const val MUSIC_RE_SORT = "music_re_sort"
    const val DOODLE_COLOR = "doodle_color"
    const val DOODLE_MOSAIC = "doodle_mosaic"
    const val DOODLE_MATERIALS = "doodle_materials"
    const val DOODLE_COLOR_LOCATION = "doodle_color_location"
    const val MEDIA_SORT = "media_sort"
    const val MEDIA_SHOW_TYPE = "media_show_type"

    //设置图片时长，运用到全部
    const val IMAGE_DURATION_APPLY_ALL = "image_duration_apply_all"

    /*bus.post*/
    const val DRAFT_SELECT_MODE = "draft_select_mode"
    const val WORK_SELECT_MODE = "work_select_mode"
    const val ADD_CLIP = "add_clip"
    const val DRAFT_EDIT_TO_CLIP = "draft_edit_to_clip"
    const val ANDROID_R_TRANSITION = "android_r_transition"

    /**
     * 裁剪范围保存
     */
    const val SAVE_LOCAL_CROP_RECT = "save_local_crop_rect"
    const val SAVE_LOCAL_MENU_SORT_THEME = "save_local_menu_sort_theme" //editor主题菜单按钮顺序

    const val LOCAL_STICKER_ID_SAVE = "local_sticker_id_save"
    private fun getSharedPreferences(): SharedPreferences {
        return MyApplication.instance.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }

    private val gson = Gson()

    /**
     * 子线程运行
     */
    fun launchIO(run: () -> Unit) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                run.invoke()
            }
        }
    }


    fun putString(key: String, value: String?) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putString(key, value)
            editor.apply()
        }

    }

    fun putInt(key: String, `val`: Int) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putInt(key, `val`)
            editor.apply()
        }
    }

    fun putLong(key: String, `val`: Long) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putLong(key, `val`)
            editor.apply()
        }
    }

    fun putBoolean(key: String, `val`: Boolean) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putBoolean(key, `val`)
            editor.apply()
        }
    }

    fun putFloat(key: String, value: Float) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putFloat(key, value)
            editor.apply()
        }
    }

    fun getFloat(key: String, defaultVal: Float): Float {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getFloat(key, defaultVal)
    }


    fun getString(key: String, defaultVal: String?): String? {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getString(key, defaultVal)
    }

    fun getInt(key: String, defaultVal: Int): Int {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getInt(key, defaultVal)
    }

    fun getLong(key: String, defaultVal: Long): Long {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getLong(key, defaultVal)
    }

    fun getBoolean(key: String, defaultVal: Boolean): Boolean {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getBoolean(key, defaultVal)
    }

    /**
     * 换成gson方案，可以拓展字段
     *
     * @param key
     * @param o
     * @param <T>
    </T> */
    fun <T> setObject(key: String?, o: Any?) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            val str: String = gson.toJson(o)
            editor.putString(key, str)
            editor.apply()
        }
    }

    /**
     * 换成gson方案，可以拓展字段
     *
     * @param key
     * @return
     */
    fun getObject(key: String?, t: Type?): Any? {
        val sp: SharedPreferences = getSharedPreferences()
        val str = sp.getString(key, "")
        return if (str == "") {
            null
        } else gson.fromJson<Any>(str, t)
    }
}