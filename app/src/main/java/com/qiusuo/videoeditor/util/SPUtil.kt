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
object SPUtil {
    private const val NAME = "VideoEdit"
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