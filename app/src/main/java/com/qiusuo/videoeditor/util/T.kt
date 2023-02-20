package com.qiusuo.videoeditor.util

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.os.Handler
import android.os.Message
import android.text.TextUtils
import android.view.WindowManager
import android.widget.Toast

object T {

    fun showShort(context: Context, text: String?) {
        showToast(context, text, true)
    }
    fun showLong(context: Context, text: String?) {
        showToast(context, text, false)
    }

    /**
     * show toast content
     *
     * @param context
     * @param text
     */
    fun showToast(context: Context, text: String?, boolShort: Boolean) {
        if (isFastDoubleClick && TextUtils.equals(text, mLastText)) {
            return
        }
        val toast: Toast = Toast.makeText(
            context, text, if (boolShort) Toast.LENGTH_SHORT else {
                Toast.LENGTH_SHORT
            }
        )
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1) {
            try {
                // 获取 mTN 字段对象
                val mTNField = Toast::class.java.getDeclaredField("mTN")
                mTNField.isAccessible = true
                val mTN = mTNField[toast]

                // 获取 mTN 中的 mHandler 字段对象
                val mHandlerField = mTNField.type.getDeclaredField("mHandler")
                mHandlerField.isAccessible = true
                val mHandler = mHandlerField[mTN] as Handler

                // 偷梁换柱
                mHandlerField[mTN] = object : Handler() {
                    @SuppressLint("HandlerLeak")
                    override fun handleMessage(msg: Message) {
                        // 捕获这个异常，避免程序崩溃
                        try {
                            mHandler.handleMessage(msg)
                        } catch (ignored: WindowManager.BadTokenException) {
                        }
                    }
                }
            } catch (ignored: IllegalAccessException) {
            } catch (ignored: NoSuchFieldException) {
            }
        }
        toast.show()
    }


    private const val TIME: Long = 1000
    private var lastClickTime: Long = 0
    private var mLastText: String? = null
    val isFastDoubleClick: Boolean
        get() {
            val time = System.currentTimeMillis()
            if (time - lastClickTime < TIME) {
                return true
            }
            lastClickTime = time
            return false
        }
}