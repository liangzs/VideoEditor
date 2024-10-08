/**
 * Kotlin扩展属性和扩展函数
 */
package com.qiusuo.videoeditor.util

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.res.Resources
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.qiusuo.videoeditor.R

/**
 * Kt扩展属性，判断Activity是否存活
 */
val Activity?.isAlive: Boolean
    get() = !(this?.isDestroyed ?: true || this?.isFinishing ?: true)

/**
 * Boolean转Visibility
 */
fun Boolean.toVisibility() = if (this) View.VISIBLE else View.GONE

/**
 * Context转Activity
 */
fun Context.getActivity(): FragmentActivity? {
    return when (this) {
        is FragmentActivity -> {
            this
        }
        is ContextWrapper -> {
            this.baseContext.getActivity()
        }
        else -> null
    }
}

fun Activity.loading(tip:String){

}

val Float.dp: Float
    get() = TypedValue.applyDimension(
        TypedValue.COMPLEX_UNIT_DIP,
        this,
        Resources.getSystem().displayMetrics
    )

val Int.dp: Float
    get() = this.toFloat().dp

val Float.half: Float
    get() = this / 2F


val Int.half: Int
    get() = (this / 2F).toInt()

val Float.radians: Float
    get() = Math.toRadians(this.toDouble()).toFloat()

fun ImageView.setImageUrl(url: String) {
    val options: RequestOptions = RequestOptions()
        .placeholder(R.color.bg_image)
        .error(R.color.bg_image)
    Glide.with(context)
        .applyDefaultRequestOptions(options)
        .load(url)
        .into(this)
}

val String?.nullSafeValue: String
    get() = this ?: ""

fun RecyclerView.ViewHolder.getResources(): Resources {
    return itemView.resources
}

fun RecyclerView.ViewHolder.getContext(): Context {
    return itemView.context
}

fun RecyclerView.ViewHolder.getActivity(): FragmentActivity {
    return getContext().getActivity()!!
}

fun <T> Result<T>.get(): T {
    return this.getOrNull()!!
}

fun Result<*>.exception(): Throwable {
    return this.exceptionOrNull()!!
}