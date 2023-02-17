package com.qiusuo.videoeditor.util

import android.content.Context
import android.content.SharedPreferences
import com.qiusuo.videoeditor.base.MyApplication
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * 存储
 */
object SpUtil {
    private const val NAME = "VideoEdit"

    /*----------------tag----------------------*/
    const val BITMAP_DOODLE_BASE_Y = "bitmap_doodle_base_y"

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
    private fun getSharedPreferences(): SharedPreferences {
        return MyApplication.instance.getSharedPreferences(NAME, Context.MODE_PRIVATE)
    }


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

    fun putString(key: String?, value: String?) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putString(key, value)
            editor.apply()
        }

    }

    fun putInt(key: String?, `val`: Int) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putInt(key, `val`)
            editor.apply()
        }
    }

    fun putLong(key: String?, `val`: Long) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putLong(key, `val`)
            editor.apply()
        }
    }

    fun putBoolean(key: String?, `val`: Boolean) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putBoolean(key, `val`)
            editor.apply()
        }
    }

    fun putFloat(key: String?, value: Float) {
        launchIO {
            val editor: SharedPreferences.Editor = getSharedPreferences().edit()
            editor.putFloat(key, value)
            editor.apply()
        }
    }

    fun getFloat(key: String?, defaultVal: Float): Float {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getFloat(key, defaultVal)
    }


    fun getString(key: String?, defaultVal: String?): String? {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getString(key, defaultVal)
    }

    fun getInt(key: String?, defaultVal: Int): Int {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getInt(key, defaultVal)
    }

    fun getLong(key: String?, defaultVal: Long): Long {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getLong(key, defaultVal)
    }

    fun getBoolean(key: String?, defaultVal: Boolean): Boolean {
        val sp: SharedPreferences = getSharedPreferences()
        return sp.getBoolean(key, defaultVal)
    }
}